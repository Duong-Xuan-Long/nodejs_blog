package com.ecommerce.library.service;


import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    //TÌm tất cả cá loại
    List<Category> findAll();

    //Lưu cá loại
    Category save(Category category);

    //Tìm theo id
    Category findById(Long id);

    //update các loại
    Category update(Category category);

    //Xóa loại theo id
    void deleteById(Long id);

    //Kích hoạt loại
    void enabledById(Long id);

    //TÌm tất cả các loại đã được kích hoạt
    List<Category> findAllByActivated();

    //Tìm tất cả các categoryDto
    List<CategoryDto> getCategoryDto();

    //Xóa vĩnh viễn một loại
    void hardDeleteCategory(Long id);
    //Phân trang các category

    Page<Category> pageCategories(int pageNo);
    //phân trang khi tìm các category

    Page<Category> searchCategories(int pageNo, String keyword);
}
