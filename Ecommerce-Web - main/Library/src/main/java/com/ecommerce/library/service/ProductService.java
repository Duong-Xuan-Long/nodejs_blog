package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductService {
    //Tìm tất cả các sp trả về dto
    List<ProductDto> findAll();

    //Lưu  sản phẩm
    Product save(MultipartFile imageProduct, ProductDto productDto);

    //update sản phẩm
    Product update(MultipartFile imageProduct, ProductDto productDto);

    //Xóa mềm  sản phẩm theo id
    void deleteById(Long id);

    //Kích hoạt sản phẩm theo id
    void enableById(Long id);

    //Lấy sp dto theo id
    public ProductDto getById(Long id);

    //Phân trang sản phẩm dto
    Page<ProductDto> pageProducts(int PageNo);

    //Trả về phân trang sản phẩm
    Page<ProductDto> pageProductsCustomer(int pageNo);

    //Trả về phân trang sản phẩm được tìm
    Page<ProductDto> searchProducts(int pageNo, String keyword);

    //Lấy danh sách sản phẩm bat ki
    List<ProductDto> getRandomProducts();

    //Lấy danh sách sản phẩm có giá cao nhất
    List<ProductDto> get5HighestPriceProducts();
    //Lấy danh sách các sp đã được kích hoạt trả về phân trang

    Page<ProductDto> searchCustomerProducts(int pageNo, String keyword);
    //Lấy danh sách các sp đã được kích hoạt trả về phân trang theo loại

    Page<ProductDto> searchCusTomerProductsByCategory(int pageNo, Long id);

    //Lấy danh sách các sản phẩm từ cao nhất
    Page<ProductDto> getDescProducts(int pageNo);

    //Lấy danh sách các sản phẩm từ thấp nhất
    Page<ProductDto> getAscProducts(int pageNo);

    //Lấy sản phẩm theo id
    Product getProductById(Long id);

    //Xóa vĩnh viễn sp
    void hardDeleteProduct(Long id);
}
