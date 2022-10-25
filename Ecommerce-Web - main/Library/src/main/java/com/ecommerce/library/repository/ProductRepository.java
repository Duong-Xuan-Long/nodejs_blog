package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Xóa vĩnh viễn sản phẩm ở trang admin
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from products p where p.product_id=?1")
    void hardDeleteProduct(Long id);

    //Tìm sản phẩm theo keyword của tang admin
    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    List<Product> searchProducts(String searchWords);

    //Tìm các sản phẩm đã được kích hoạt
    @Query("select p from Product p where p.is_activated=true and p.is_deleted=false")
    List<Product> searchByActivated();

    //Tìm các sản phẩm sắp xếp từ giá cao nhất
    @Query(nativeQuery = true, value = "select * from products p WHERE p.is_activated=true and p.is_deleted=false order by p.sale_price DESC limit 6;")
    List<Product> tophighestPriceProduct();

    //Tìm các sản phẩm sắp xếp từ giá thấp nhất
    @Query(nativeQuery = true, value = "select * from products p where (p.description like '%w%' or p.name like '%w%') AND p.is_activated=true and p.is_deleted=false")
    List<Product> searchCustomerProducts();

    //Tìm các sản phẩm theo loại
    @Query(nativeQuery = true, value = "select * from products p where (p.category_id=?1) AND p.is_activated=true and p.is_deleted=false")
    List<Product> searchCusTomerProductsByCategory(Long id);

    //Tìm các sản phẩm sắp xếp từ thấp đến cao
    @Query(nativeQuery = true, value = "SELECT * \n" +
            " from products p \n" +
            " WHERE p.is_activated=true and p.is_deleted=false\n" +
            "ORDER BY p.sale_price DESC")
    List<Product> getDescProducts();

    //Tìm các sản phẩm sắp xếp từ cao đến thấp
    @Query(nativeQuery = true, value = "SELECT * \n" +
            " from products p \n" +
            " WHERE p.is_activated=true and p.is_deleted=false\n" +
            "ORDER BY p.sale_price ASC")
    List<Product> getAscProducts();
}
