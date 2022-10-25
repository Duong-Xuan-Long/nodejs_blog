package com.ecommerce.customer;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class CustomerApplicationTests {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    AdminService adminService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    CustomerService customerService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    ProductService productService;

    @Test
    void delete() {
        Customer customer = customerRepository.findById(6L).get();
        orderService.saveOrder(customer.getShoppingCart());

    }

    @Test
    void save() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName("customerNew");
        customerDto.setLastName("customerNew");
        customerDto.setUsername("customerNew@gmail.com");
        customerDto.setPassword(bCryptPasswordEncoder.encode("123456"));
        customerService.save(customerDto);
    }

    @Test
    void findCustomerByUsername() {
        customerService.findByUsername("customer1");
    }

    @Test
    void updateCustomer() {
        Customer customer = customerRepository.findById(9L).get();
        customerService.update(null, customer);
    }

    @Test
    void addItemToCart() {
        Product product = productService.getProductById(1L);
        Customer customer = customerRepository.findById(6L).get();
        shoppingCartService.addItemtoCart(product, 1, customer);
    }

    @Test
    void updateItem() {
        Product product = productService.getProductById(1L);
        Customer customer = customerRepository.findById(6L).get();
        shoppingCartService.updateItemInCart(product, 2, customer);
    }

    @Test
    void deleteItem() {
        Product product = productService.getProductById(1L);
        Customer customer = customerRepository.findById(6L).get();
        shoppingCartService.deleteItemInCart(product, customer);
    }

    @Test
    void acceptOrder() {
        orderService.acceptOrder(17L);
    }

    @Test
    void deleteOrder() {
        orderService.cancelOrder(17L);
    }
}
