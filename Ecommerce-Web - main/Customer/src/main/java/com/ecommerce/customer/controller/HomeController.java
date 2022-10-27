package com.ecommerce.customer.controller;

import com.ecommerce.customer.request.LoginRequest;
import com.ecommerce.customer.service.AuthService;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    public ProductService productService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    CustomerService customerService;


    //Trả về trang chủ
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String getIndexPage(Model model, Principal principal, HttpSession session, RedirectAttributes redirectAttributes) {

        if (principal != null) {
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUsername(principal.getName());

            if (customer.is_activated() == false) {
                redirectAttributes.addFlashAttribute("failed", "Tài khoản chưa được kích hoạt");
                return "redirect:/login?logout";
            }
            ShoppingCart cart = customer.getShoppingCart();
            if (cart != null) {
                session.setAttribute("totalItems", cart.getTotalItems());
            }
            session.setAttribute("totalItems", 0);
        } else {
            session.removeAttribute("username");
        }
        model.addAttribute("randomProducts", productService.getRandomProducts());
        model.addAttribute("categories", categoryService.findAllByActivated());
        model.addAttribute("highestPriceProducts", productService.get5HighestPriceProducts());
        return "index";
    }

    //Trả về trang tĩnh about
    @GetMapping("/about")
    public String getAboutPage(Principal principal,HttpSession session) {
        if(principal!=null){
            session.setAttribute("username", principal.getName());
        }else{
            session.removeAttribute("username");
        }
        return "about";
    }
}
