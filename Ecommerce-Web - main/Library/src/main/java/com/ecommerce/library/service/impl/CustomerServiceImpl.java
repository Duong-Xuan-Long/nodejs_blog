package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.utils.ImageUpload;
import com.ecommerce.library.utils.ImageUploadCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ImageUploadCustomer imageUploadCustomer;

    @Autowired
    private RoleRepository roleRepository;

    //lưu customer
    @Override
    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setUsername(customerDto.getUsername());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        customer.set_activated(true);
        customer.set_deleted(false);
        return transfer(customerRepository.save(customer));
    }

    //Tìm theo tên
    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).get();
    }

    //update customer
    @Override
    public Customer update(MultipartFile imageCustomer, Customer customer) {
        try {
            Customer customer1 = customerRepository.findById(customer.getId()).get();
            if (imageCustomer == null) {
                customer1.setImage(null);
            } else {
                if (!imageCustomer.getOriginalFilename().equals("")) {
                    if (imageUploadCustomer.checkExisted(imageCustomer) == false) {
                        imageUploadCustomer.uploadImage(imageCustomer);
                    }
                    customer1.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));
                } else {
                    customer1.setImage(customer1.getImage());
                }
            }

            customer1.setFirstName(customer.getFirstName());
            customer1.setLastName(customer.getLastName());
            customer1.setUsername(customer.getUsername());
            customer1.setCountry(customer.getCountry());
            customer1.setPhoneNumber(customer.getPhoneNumber());
            customer1.setAddress(customer.getAddress());
            customer1.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer1.setCity(customer.getCity());
            return customerRepository.save(customer1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Tạo page customer
    @Override
    public Page<Customer> pageCustomer(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<Customer> customers = customerRepository.findAll();
        Page<Customer> customerPages = toPage(customers, pageable);
        return customerPages;
    }

    //page các sản phẩm được tìm
    @Override
    public Page<Customer> searchProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        Page<Customer> customerPages = toPage(customers, pageable);
        return customerPages;
    }

    //xóa mềm customer
    @Override
    public void deleteById(Long id) {
        Customer customer = customerRepository.findById(id).get();
        customer.set_deleted(true);
        customer.set_activated(false);
        customerRepository.save(customer);
    }

    //Kích hoạt customer
    @Override
    public void enableById(Long id) {
        Customer customer = customerRepository.findById(id).get();
        customer.set_deleted(false);
        customer.set_activated(true);
        customerRepository.save(customer);
    }

    //Xóa vĩnh viễn customer
    @Override
    public void hardDelete(Long id) {
        customerRepository.harddeleteCustomerById(id);
    }

    //chuyển customer thành customerDto
    private CustomerDto transfer(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        return customerDto;
    }

    //Xây dựng page
    private Page toPage(List<Customer> list, Pageable pageable) {
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
