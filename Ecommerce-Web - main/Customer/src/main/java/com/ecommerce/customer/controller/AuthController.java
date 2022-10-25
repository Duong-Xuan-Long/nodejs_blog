package com.ecommerce.customer.controller;

import com.ecommerce.customer.exception.BadRequestException;
import com.ecommerce.customer.service.ForgotPasswordService;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    CustomerService customerService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ForgotPasswordService forgotPasswordService;

    //Trả về trang login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Trả về trang đăng kí
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }

    //Xử lí quá trình đăng kí
    @PostMapping("/do-register")
    public String doRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                             BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            Customer customer = customerService.findByUsername(customerDto.getUsername());
            if (customer != null) {
                model.addAttribute("username", "Username has been registered");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success", "Register successfully");
                return "register";
            } else {
                model.addAttribute("password", "Password is not same");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Server have ran some problems");
            model.addAttribute("customerDto", customerDto);
            System.out.println(2);
        }
        return "register";
    }

    //Link đến trang xử lí quên mật khẩu
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {

        return "forgot-password";
    }

    //LInk làm quá trình lấy lại mật khẩu
    @GetMapping("/do-forgot-password")
    public String forgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            forgotPasswordService.forgotPassword(email);
            redirectAttributes.addFlashAttribute("success", "Login with new password");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/login";
    }
}
