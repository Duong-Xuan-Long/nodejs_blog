package com.ecommerce.customer.controller;

import com.ecommerce.customer.exception.BadRequestException;
import com.ecommerce.customer.request.LoginRequest;
import com.ecommerce.customer.request.RegisterRequest;
import com.ecommerce.customer.service.AuthService;
import com.ecommerce.customer.service.ForgotPasswordService;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Token;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AuthController {
    @Autowired
    CustomerService customerService;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ForgotPasswordService forgotPasswordService;

    @Autowired
    AuthService authService;

    //Trả về trang login
    @GetMapping("/login")
    public String login(Principal principal, HttpSession session) {
        if(principal!=null){
            session.setAttribute("username", principal.getName());
        }else{
            session.removeAttribute("username");
        }
        return "login";
    }

    @PostMapping("/do-login")
    @ResponseBody
    public String loginProcess(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse){
        return  authService.login(request, httpServletResponse);
    }

    @GetMapping("/logout-handle")
    public String logout(HttpServletResponse httpServletResponse,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success","Bạn đã đăng xuất thành công");
        authService.logout(httpServletResponse);
        return "redirect:/login";
    }

    //Trả về trang đăng kí
    @GetMapping("/register")
    public String register(Model model,Principal principal,HttpSession session) {
        if(principal!=null){
            session.setAttribute("username", principal.getName());
        }else{
            session.removeAttribute("username");
        }
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }
    @GetMapping("/confirm")
    @ResponseBody
    public String confirm(@RequestParam String token){
        return authService.confirmToken(token);
    }

    //Xử lí quá trình đăng kí
    @PostMapping("/do-register")
    @ResponseBody
    public String doRegister(@RequestBody RegisterRequest registerRequest) throws MessagingException {
        return authService.register(registerRequest);
    }

    //Link đến trang xử lí quên mật khẩu
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model,Principal principal,HttpSession session) {
        if(principal!=null){
            session.setAttribute("username", principal.getName());
        }else{
            session.removeAttribute("username");
        }
        return "forgot-password";
    }

    //LInk làm quá trình lấy lại mật khẩu
    @GetMapping("/do-forgot-password")
    public String forgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            forgotPasswordService.forgotPassword(email);
            redirectAttributes.addFlashAttribute("success", "Đăng nhập với mật khẩu mới");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/login";
    }
}
