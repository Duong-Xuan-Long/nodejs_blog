package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    //Lưu loại
    @Override
    public Category save(Category category) {
        Category category1 = new Category(category.getName());
        return categoryRepository.save(category1);
    }

    //tìm theo id
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    //update loại
    @Override
    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate = categoryRepository.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_deleted(category.is_deleted());
            categoryUpdate.set_activated(categoryUpdate.is_activated());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryRepository.save(categoryUpdate);
    }

    //xóa mềm loại
    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).get();
        category.set_deleted(true);
        category.set_activated(false);
        categoryRepository.save(category);
    }

    //Kích hoạt loại
    @Override
    public void enabledById(Long id) {
        Category category = categoryRepository.findById(id).get();
        category.set_activated(true);
        category.set_deleted(false);
        categoryRepository.save(category);
    }

    //Tìm các loại được kích hoạt
    @Override
    public List<Category> findAllByActivated() {
        return categoryRepository.findAllByActivated();
    }

    //Lấy các categoryDto
    @Override
    public List<CategoryDto> getCategoryDto() {
        return categoryRepository.getCategoryDto();
    }

    //Xóa vĩnh viễn một loại
    @Override
    public void hardDeleteCategory(Long id) {
        categoryRepository.hardDeleteCategory(id);
    }

    //Lấy các trang của loại
    @Override
    public Page<Category> pageCategories(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<Category> categories = categoryRepository.findAll();
        Page<Category> categoryPages = toPage(categories, pageable);
        return categoryPages;
    }

    //Tìm các category theo trang
    @Override
    public Page<Category> searchCategories(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<Category> categories = categoryRepository.searchCategories(keyword);
        Page<Category> categoryPages = toPage(categories, pageable);
        return categoryPages;
    }

    //Xây dựng trang của loại
    private Page toPage(List<Category> list, Pageable pageable) {
        //offset=pageNo*size
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size()) ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }
}
