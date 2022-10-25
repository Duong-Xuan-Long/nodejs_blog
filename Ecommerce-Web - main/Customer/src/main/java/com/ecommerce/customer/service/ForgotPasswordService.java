package com.ecommerce.customer.service;


import com.ecommerce.customer.exception.BadRequestException;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ForgotPasswordService {
    @Autowired
    EmailService emailService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    CustomerService customerService;

    //gửi mail mật khẩu mới quên mật khẩu
    public void forgotPassword(String email) {
        Customer customer = customerService.findByUsername(email);
        if (customer == null) {
            throw new BadRequestException("email : " + email + " doesn't exist");
        }
        String newPassword=generateUUDI();
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        emailService.send(email, "Forgot Password", "New Password:" + newPassword);
    }

    //Tạo mật khẩu mới
    private String generateUUDI() {
        String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
        String alphaUpperCase = alpha.toUpperCase(); // A-Z
        String digits = "0123456789"; // 0-9
        String specials = "~=+%^*/()[]{}/!@#$?|";
        String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
        String ALL = alpha + alphaUpperCase + digits + specials;
        Random generator = new Random();
        StringBuilder sb = new StringBuilder();
        int min = 0;
        int max = ALPHA_NUMERIC.length() - 1;
        for (int i = 0; i < 5; i++) {
            int number = generator.nextInt((max - min) + 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }
}
