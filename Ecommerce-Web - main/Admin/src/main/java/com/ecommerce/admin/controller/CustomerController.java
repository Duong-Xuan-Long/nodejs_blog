package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;

    //trang customer có phân trang
    @GetMapping("/customers/page/{pageNo}")
    public String customersPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<Customer> customers = customerService.pageCustomer(pageNo-1);
        model.addAttribute("size", customers.getSize());
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("customers", customers);
        return "customers";
    }

    //trang kết quả tìm customer
    @GetMapping("detail-customer/{id}")
    public String detailedCustomerPage(@PathVariable("id") Long id, Model model) {
        Customer customer = customerRepository.findById(id).get();
        model.addAttribute("customer", customer);
        return "detailedCustomer";
    }

    //kích hoạt customer
    @GetMapping("/enable-customer/{id}")
    public String enableCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Kích hoạt thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Kích hoạt thất bại!");
        }
        customerService.enableById(id);
        return "redirect:/customers/page/1";
    }

    //xóa mềm customer
    @GetMapping("/delete-customer/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Xóa thất bại");
        }

        return "redirect:/customers/page/1";
    }

    //xóa xĩnh viễn customer
    @GetMapping("hard-delete-customer/{id}")
    public String hardDeleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa vĩnh viễn thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Xóa vĩnh viễn thất bại");
        }

        return "redirect:/customers/page/1";
    }

    //kết quả trang tìm customer
    @GetMapping("/search-customers/page/{pageNo}")
    public String getSearchPage(@PathVariable int pageNo, @RequestParam("keyword") String keyword,
                                Principal principal,
                                Model model) {

        if (principal == null) {
            return "redirect:/login";
        }
        Page<Customer> customers = customerService.searchProducts(pageNo-1, keyword);
        model.addAttribute("size", customers.getSize());
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("customers", customers);
        model.addAttribute("keyword",keyword);
        return "result_customer";
    }
}
