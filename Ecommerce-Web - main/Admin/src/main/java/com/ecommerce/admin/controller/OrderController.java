package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
//get ALl
    @GetMapping("/orders/page/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<Order> orders = orderService.getAllOrder(pageNo-1);
        model.addAttribute("size", orders.getSize());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("orders", orders);
        return "orders";
    }
//Xóa order
    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.cancelOrder(id);
        return "redirect:/orders/page/1";
    }
    //accept order
    @GetMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable("id")Long id, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.acceptOrder(id);
        return "redirect:/orders/page/1";
    }
   // unaccept order
    @GetMapping("/unaccept-order/{id}")
    public String unAcceptOrder(@PathVariable("id")Long id, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.undoAcceptOrder(id);
        return "redirect:/orders/page/1";
    }
    //update notes
    @GetMapping("/update-order/{id}")
    public String updateOrder(@PathVariable("id")Long id, @RequestParam("notes")String notes, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        Order order=orderRepository.findById(id).get();
        orderService.updateNote(id,notes);
        return "redirect:/orders/page/1";
    }

}
