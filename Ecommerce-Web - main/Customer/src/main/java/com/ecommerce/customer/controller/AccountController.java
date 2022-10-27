package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class AccountController {
    @Autowired
    CustomerService customerService;

    //Trả về trang my account
    @GetMapping("/account")
    public String getAccountHome(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            session.removeAttribute("username");
            return "redirect:/login";
        }else{
            session.setAttribute("username", principal.getName());
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        model.addAttribute("customer", customer);
        return "my-account";
    }

    //update customer
    @PostMapping("/update-customer")
    public String updateCustomer(@ModelAttribute("customer") Customer customer,
                                 @RequestParam("imageCustomer") MultipartFile imageCustomer,
                                 RedirectAttributes attributes) {
        try {
            customerService.update(imageCustomer, customer);
            attributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Cập nhật thất bại!");
        }
        return "redirect:/account";
    }
}
