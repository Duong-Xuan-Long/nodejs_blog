package com.ecommerce.customer.service;

import com.ecommerce.customer.config.CustomerDetails;
import com.ecommerce.customer.config.CustomerServiceConfig;
import com.ecommerce.customer.config.JwtUtils;
import com.ecommerce.customer.exception.BadRequestException;
import com.ecommerce.customer.exception.NotFoundException;
import com.ecommerce.customer.request.LoginRequest;
import com.ecommerce.customer.request.RegisterRequest;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.model.Token;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

import static com.ecommerce.customer.constant.Constant.MAX_AGE_COOKIE;


@Service
public class AuthService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private EmailService mailService;

    @Autowired
    CustomerServiceConfig customerServiceConfig;
    @Autowired
    RoleRepository roleRepository;

    // LOGIN USER
    public String login(LoginRequest request, HttpServletResponse httpServletResponse) {
        try {

            // T???o ?????i t?????ng x??c th???c d???a tr??n th??ng tin request
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            // Ti???n h??nh x??c th???c
            Authentication authentication = daoAuthenticationProvider.authenticate(authenticationToken);

            // L??u tr??? th??ng tin c???a ?????i t?????ng ???? ????ng nh???p
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtUtils.generateToken((CustomerDetails) authentication.getPrincipal());

            // L??u th??ng tin v??o trong cookie(neu khong dung cookie thi tra thang token ve cho client
            // va moi request client gui len phai kem theo token trong header cua request)
            Cookie cookie = new Cookie("JWT_COOKIE", token);
            cookie.setPath("/");
            cookie.setMaxAge(MAX_AGE_COOKIE);
            cookie.setHttpOnly(true);//khong cho client sua cookie
            httpServletResponse.addCookie(cookie);
            // Tr??? v??? token cho client
            return token;
        } catch (Exception ex) {
            throw new BadRequestException("Email ho???c m???t kh???u kh??ng ch??nh x??c");
        }
    }

    // LOGOUT USER
    public String logout(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("JWT_COOKIE", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        httpServletResponse.addCookie(cookie);

//        Cookie cookie1 = new Cookie("JSESSIONID", null); // Not necessary, but saves bandwidth.
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
//        httpServletResponse.addCookie(cookie1);
        return "logout success";
    }

     //REGISTER USER
    public String register(RegisterRequest request) throws MessagingException {
        // L???y th??ng tin user d???a tr??n email
        Optional<Customer> customerOptional = customerRepository.findByUsername(request.getEmail());

        if (customerOptional.isPresent()) {
            // N???u user ???????c t??m th???y c?? tr??ng c??c thu???c t??nh v?? ch??a ???????c k??ch ho???t
            // G???i email k??ch ho???t
            Customer customer = customerOptional.get();
            if (!customer.getEnabled()
                    && customer.getFirstName().equals(request.getFirstName())
                    && customer.getLastName().equals(request.getLastName())
                    && customer.getUsername().equals(request.getEmail())) {
                return generateTokenAndSendMail(customer);
            }

            throw new BadRequestException("Email = " + request.getEmail() + " ???? t???n t???i");
        }

        // M?? h??a password
        String passwordEncode = passwordEncoder.encode(request.getPassword());

        // T???o user v?? l??u v??o database
        Role role=roleRepository.findByName("CUSTOMER");
        Collection<Role> roles=List.of(role);
        Customer newCustomer = new Customer(request.getFirstName(), request.getLastName(),
                request.getEmail(), passwordEncode, roles);
        newCustomer.set_activated(true);
        newCustomer.set_deleted(false);
        customerRepository.save(newCustomer);

        // Sinh ra token
        return generateTokenAndSendMail(newCustomer);
    }

    // SINH TOKEN - SEND MAIL
    private String generateTokenAndSendMail(Customer customer) throws MessagingException {
        // Sinh ra token
        String tokenString = UUID.randomUUID().toString();

        // T???o token v?? l??u token
        Token token = new Token(
                tokenString,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                customer
        );
        tokenRepository.save(token);

        // G???i email
        String link="<p>Nh???n v??o ???????ng link ????? k??ch ho???t t??i kho???n</p>";
        link += "<a href=" +"http://localhost:8020/shop/confirm?token="+ tokenString+
                ">Link k??ch ho???t t??i kho???n</a>"  ;
        mailService.send(customer.getUsername(), "X??c th???c t??i kho???n", link);

        return link;
    }

    // VERIFY TOKEN
    public String confirmToken(String tokenString) {
        // L???y th??ng tin c???a token
        Token token = tokenRepository.findByToken(tokenString).orElseThrow(() ->
                new NotFoundException("Kh??ng t??m th???y token")
        );

        // Xem token ???? ???????c confirm hay ch??a
        if (token.getConfirmedAt() != null) {
            throw new BadRequestException("Token ???? ???????c x??c th???c");
        }

        // Xem token ???? h???t h???n ch??a
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token ???? h???t th???i gian");
        }

        // Active token
        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);

        // Active user
        Customer customer = token.getCustomer();
        customer.setEnabled(true);
        customerRepository.save(customer);

        return "confirmed";
    }
}
