package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    AdminRepository adminRepository;

    //trang update admin
    @GetMapping("/update-admin/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal, HttpSession httpSession) {
        if (principal == null) {
            return "redirect:/login";
        }
        Admin admin = adminRepository.findById(id).get();
        httpSession.setAttribute("adminSrc",admin.getImage());
        model.addAttribute("admin", admin);
        return "update-admin";
    }

    //QUá trình update admin
    @PostMapping("/update-admin/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("admin") Admin admin,
                                @RequestParam("imageAdmin") MultipartFile imageAdmin,
                                RedirectAttributes attributes
    ) {
        try {
            adminService.update(imageAdmin, admin);
            attributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to update!");
        }
        return "redirect:/update-admin/" + admin.getId();

    }
}
