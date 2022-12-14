package com.ecommerce.admin.controller;

import com.ecommerce.admin.exception.BadRequestException;
import com.ecommerce.admin.service.EmailService;
import com.ecommerce.admin.service.ForgotPasswordService;
import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class LoginController {
    @Autowired
    ForgotPasswordService forgotPasswordService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    //trang index admin
    @RequestMapping("/index")
    public String home(Model model, Principal principal, HttpSession httpSession){
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    if(authentication==null||authentication instanceof AnonymousAuthenticationToken){
        return "redirect:/login";
    }
    if(principal!=null){
        Admin admin=adminService.findByUsername(principal.getName());
        httpSession.setAttribute("idAdmin",admin.getId());
        httpSession.setAttribute("adminSrc",admin.getImage());
    }else{
        httpSession.removeAttribute("idAdmin");
        httpSession.removeAttribute("adminSrc");
    }
    model.addAttribute("totalProducts",productService.findAll().size());
    model.addAttribute("totalCategories",categoryService.findAll().size());
        return "index";
    }
    //Trang ????ng k?? admin
   @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
   }
//Trang qu??n m???t kh???u
@GetMapping("/forgot-password")
public String forgotPassword(Model model) {

    return "forgot-password";
}
//link ????? l??m ????ng k??
   @PostMapping("/register-new")
   public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
                             BindingResult result,
                             Model model){

        try {

            if(result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDto", adminDto);
               model.addAttribute("emailError", "Email ???? ???????c d??ng!");
                return "register";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
//                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
               model.addAttribute("success", "????ng k?? th??nh c??ng!");
                model.addAttribute("adminDto", adminDto);
            }else{
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Tr??ng m???t kh???u ki???m tra l???i!");
                return "register";
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errors", "L???i m??y ch???!");
        }
        return "register";

   }
//LInk l??m qu?? tr??nh l???y l???i m???t kh???u
@GetMapping("/do-forgot-password")
public String forgotPassword(@RequestParam("email")String email, RedirectAttributes redirectAttributes){
    try{
        forgotPasswordService.forgotPassword(email);
        redirectAttributes.addFlashAttribute("success","????ng nh???p v???i m???t kh???u m???i t??? email c???a b???n");
    }catch (BadRequestException e){
        redirectAttributes.addFlashAttribute("error",e.getMessage());
    }

    return "redirect:/login";
}

}
