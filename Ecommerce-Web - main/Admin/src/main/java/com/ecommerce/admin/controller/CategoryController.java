package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //Link lấy trang category có phân trang
    @GetMapping("/categories/page/{pageNo}")
    public String getCategoryPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<Category> categories = categoryService.pageCategories(pageNo-1);
        model.addAttribute("size", categories.getSize());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCreate", new Category());
        return "categories";
    }

    //trang kết quả tìm category
    @GetMapping("/search-categories/page/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model,
                                 Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<Category> categories = categoryService.searchCategories(pageNo-1, keyword);
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("categoryCreate", new Category());
        model.addAttribute("keyword",keyword);
        return "result-categories";
    }

    //Thêm Category
    @PostMapping("/add-category")
    public String add(@ModelAttribute("categoryCreate") Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("success", "Thêm thành công");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Đã tồn tại loại này");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Thêm thất bại ");
        }
        return "redirect:/categories/page/1";
    }

    //Tìm category bằng id
    @RequestMapping(value = "/findById", method = {RequestMethod.GET})
    @ResponseBody
    public Category findById(@RequestParam Long id) {
        return categoryService.findById(id);
    }

    //update category
    @GetMapping("update-category")
    public String update(Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Cập nhật thất bại vì trùng tên");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Lỗi máy chủ");
        }
        return "redirect:/categories/page/1";
    }

    //Xóa category
    @GetMapping("/delete-category")
    public String deleteCategory(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Xóa thất bại");
        }
        return "redirect:/categories/page/1";
    }

    //CHo category active
    @GetMapping("/enable-category")
    public String enableCategory(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.enabledById(id);
            redirectAttributes.addFlashAttribute("success", "Kích hoạt thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Kích hoạt thất bại");
        }
        return "redirect:/categories/page/1";
    }

    @GetMapping("hard-delete-category/{id}")
    public String hardDeleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.hardDeleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa vĩnh viễn thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Xóa vĩnh viễn thất bại");
        }
        return "redirect:/categories/page/1";
    }
}
