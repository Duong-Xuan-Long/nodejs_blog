package com.ecommerce.library.repository;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //xóa loại
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from categories c where c.category_id=?1")
    void hardDeleteCategory(Long id);

    //Tìm loại theo keyword
    @Query("select c from Category c where c.name like %?1% ")
    List<Category> searchCategories(String searchWords);

    //tìm các loai được kích hoạt cho trang customer
    @Query("select c from Category c where c.is_activated=true and c.is_deleted=false")
    List<Category> findAllByActivated();

    //tìm và trả về categorydto
    @Query("SELECT new com.ecommerce.library.dto.CategoryDto(c.id,c.name,COUNT(p.category.id)) FROM Product p INNER JOIN Category c ON p.category.id=c.id WHERE p.is_activated=true AND p.is_deleted=false GROUP BY c.id")
    List<CategoryDto> getCategoryDto();
}
