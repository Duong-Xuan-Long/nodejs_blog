package com.ecommerce.admin;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class AdminApplicationTests {


    @Autowired
    AdminService adminService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @Test
    void findByUserName() {
        adminService.findByUsername("admin1");
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void saveAdmin() {
        AdminDto adminDto = new AdminDto();
        adminDto.setFirstName("admin5");
        adminDto.setLastName("admin5");
        adminDto.setUsername("admin5@gmail.com");
        adminDto.setPassword(bCryptPasswordEncoder.encode("123456"));
        adminService.save(adminDto);
    }

    @Test
    void findALlCategory() {
        categoryService.findAll();
    }

    @Test
    void saveCategory() {
        Category category = new Category();
        category.setName("sofa");
        categoryService.save(category);
    }

    @Test
    void findById() {
        categoryService.findById(2L);
    }

    @Test
    void update() {
        Category category = categoryService.findById(3L);
        category.setName("Sofa");
        categoryService.update(category);
    }

    @Test
    void deleteById() {
        categoryService.deleteById(3L);
    }

    @Test
    void enableById() {
        categoryService.enabledById(3L);
    }

    @Test
    void findAllByActivated() {
        categoryService.findAllByActivated();
    }

    @Test
    void getCategoryDto() {
        categoryService.getCategoryDto();
    }

    @Test
    void getAllProduct() {
        productService.findAll();
    }

    @Test
    void saveProduct() {
        Category category = categoryService.findById(3L);
        ProductDto productDto = new ProductDto();
        productDto.setName("WINE RED VELVET DOUBLE SEATED CHESTERFIELD SOFA");
        productDto.setCategory(category);
        productDto.setDescription("This beautiful Wine Red Velvet Double Seated Chesterfield Sofa has been upholstered in a luxurious wine red cotton velvet and features four turned feet constructed from 100% solid mango wood in a chestnut finish. The sofa is detailed with deep button tufts, piping and sheltering arms. This is a statement piece which can be added as a splash of colour to any room in the home. It's suitable for many different styles and colours of interiors either modern or classic.");
        productDto.setCurrentQuantity(5);
        productDto.setCostPrice(600);
        productService.save(null, productDto);
    }

    @Test
    void UpdateProduct() {
        Category category = categoryService.findById(3L);
        ProductDto productDto = new ProductDto();
        productDto.setId(9L);
        productDto.setName("WINE RED VELVET DOUBLE SEATED CHESTERFIELD SOFA");
        productDto.setCategory(category);
        productDto.setDescription("This beautiful Wine Red Velvet Double Seated Chesterfield Sofa has been upholstered in a luxurious wine red cotton velvet and features four turned feet constructed from 100% solid mango wood in a chestnut finish. The sofa is detailed with deep button tufts, piping and sheltering arms. This is a statement piece which can be added as a splash of colour to any room in the home. It's suitable for many different styles and colours of interiors either modern or classic.");
        productDto.setCurrentQuantity(2);
        productDto.setCostPrice(500);
        productService.update(null, productDto);
    }

    @Test
    void deleteProductById() {
        productService.deleteById(1L);
    }

    @Test
    void enableProductById() {
        productService.enableById(1L);
    }

    @Test
    void getProductDtoById() {
        productService.getProductById(1L);
    }

    @Test
    void pageProducts() {
        productService.pageProducts(1);
    }

    @Test
    void pageProductsCustomer() {
        productService.pageProductsCustomer(1);
    }

    @Test
    void searchProducts() {
        productService.searchProducts(1, "b");
    }

    @Test
    void getRandomProducts() {
        productService.getRandomProducts();
    }

    @Test
    void get5HighestPriceProducts() {
        productService.get5HighestPriceProducts();
    }

    @Test
    void searchCustomerProducts() {
        productService.searchCustomerProducts(1, "b");
    }

    @Test
    void searchCusTomerProductsByCategory() {
        Category category = categoryService.findById(3L);
        productService.searchCusTomerProductsByCategory(1, 1L);
    }

    @Test
    void getDescProducts() {
        productService.getDescProducts(1);
    }

    @Test
    void getAscProducts() {
        productService.getAscProducts(1);
    }

    @Test
    void getProductById() {
        productService.getById(1L);
    }
}
