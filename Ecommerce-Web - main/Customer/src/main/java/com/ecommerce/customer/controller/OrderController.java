package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;

    //Trả về trang checkout
    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            session.removeAttribute("username");
            return "redirect:/login";
        }else{
            session.setAttribute("username", principal.getName());
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.getPhoneNumber() == null || customer.getAddress() == null || customer.getCity() == null
                || customer.getCountry() == null) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "Bạn phải điền hết tất cả các thông tin trước khi kiểm tra đơn!");
            return "my-account";
        }
        if (customer.getPhoneNumber().trim().isEmpty() || customer.getAddress().trim().isEmpty() || customer.getCity().trim().isEmpty()
                || customer.getCountry().trim().isEmpty()) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "Bạn phải điền hết tất cả các thông tin trước khi kiểm tra đơn!");
            return "my-account";
        }

        model.addAttribute("customer", customer);
        ShoppingCart cart = customer.getShoppingCart();
        model.addAttribute("cart", cart);
        return "checkout";
    }

    //Trả về trang order
    @GetMapping("/order")
    public String getOrderPage(Principal principal, Model model,HttpSession session) {
        if (principal == null) {
            session.removeAttribute("username");
            return "redirect:/login";
        }else{
            session.setAttribute("username", principal.getName());
        }
        Customer customer = customerService.findByUsername(principal.getName());
        List<Order> orderList = customer.getOrders();
        model.addAttribute("orders", orderList);
        model.addAttribute("size", orderList.size());
        return "order";
    }

    //Xử lí quá trình lưu order
    @GetMapping("/save-order")
    public String saveOrder(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart cart = customer.getShoppingCart();
        orderService.saveOrder(cart);
        return "redirect:/order";
    }

    //Xử lí quá trình xóa order
    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
        return "redirect:/order";
    }
}
