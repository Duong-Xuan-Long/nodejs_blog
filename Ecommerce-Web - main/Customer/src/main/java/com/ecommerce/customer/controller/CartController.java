package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    //Trả về trang shopping cart
    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        double subTotal;
        if (shoppingCart == null) {
            model.addAttribute("check", "You have no items Here");
            subTotal = 0;
        } else {
            subTotal = shoppingCart.getTotalPrices();
            subTotal = Double.parseDouble(String.format("%.2f", subTotal));
        }
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shoppingCart", shoppingCart);
        return "cart";
    }

    //Xử lí quá trình thêm item
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestBody String productId,
                            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                            Principal principal,
                            Model model,
                            HttpServletRequest request) {

        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(Long.parseLong(productId
                .substring(productId.lastIndexOf('=') + 1)));
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = shoppingCartService.addItemtoCart(product, quantity, customer);
        return "redirect:" + request.getHeader("Referer");

    }

    //update cart
    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") Long productId,
                             Principal principal, Model model
    ) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getProductById(productId);
            ShoppingCart cart = shoppingCartService.updateItemInCart(product, quantity, customer);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }
    }

    //Xử lí xóa item
    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteCart(@RequestParam("id") Long productId, Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getProductById(productId);
            ShoppingCart cart = shoppingCartService.deleteItemInCart(product, customer);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }

    }

}
