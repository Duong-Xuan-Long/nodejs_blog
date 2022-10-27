package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {
    //Lưu các customer
    CustomerDto save(CustomerDto customerDto);

    //TÌm các customer theo tên
    Customer findByUsername(String username);

    //update các customer
    Customer update(MultipartFile imageCustomer, Customer customer);

    //Phân trang customer
    Page<Customer> pageCustomer(int pageNo);

    //Trả về phân trang customer
    Page<Customer> searchProducts(int pageNo, String keyword);

    //Xóa mềm customer
    void deleteById(Long id);

    //Kích hoạt customer
    void enableById(Long id);

    //Xóa vĩnh viễn customer
    void hardDelete(Long id);
}
