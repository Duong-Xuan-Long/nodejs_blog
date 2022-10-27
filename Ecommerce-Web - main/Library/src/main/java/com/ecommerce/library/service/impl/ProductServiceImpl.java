package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;

    //Tìm tất cả sản phẩm
    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    //Lưu sản phẩm
    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product product = new Product();
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                if (imageUpload.uploadImage(imageProduct)) {
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //update sản phẩm
    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product product = productRepository.findById(productDto.getId()).get();
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                if (!imageProduct.getOriginalFilename().equals("")) {
                    if (imageUpload.checkExisted(imageProduct) == false) {
                        imageUpload.uploadImage(imageProduct);
                    }
                    product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                } else {
                    product.setImage(product.getImage());
                }
            }

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Xóa sản phẩm
    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).get();
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    //Kích hoạt sản phẩm
    @Override
    public void enableById(Long id) {
        Product product = productRepository.findById(id).get();
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    //Lấy sản phẩm theo id
    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setImage(product.getImage());
        productDto.set_deleted(product.is_deleted());
        productDto.set_activated(product.is_activated());
        return productDto;
    }

    //Trả lại sublist product của trang admin
    @Override
    public Page<ProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> products = transfer(productRepository.findAll());
        Page<ProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    //Trả lại sublist product của trang shop
    @Override
    public Page<ProductDto> pageProductsCustomer(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> products = transfer(productRepository.searchByActivated());
        Page<ProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    ////Trả lại sublist product của các sản phẩm tìm được
    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(productRepository.searchProducts(keyword));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    //Lấy các sản phẩm bất kì
    @Override
    public List<ProductDto> getRandomProducts() {
        List<Product> list = productRepository.searchByActivated();
        if (list.size() < 6) return transfer(list);
        return transfer(list.subList(0, 5));
    }

    //Lấy 6 sản phẩm giá cao nhất
    @Override
    public List<ProductDto> get5HighestPriceProducts() {
        List<Product> list = productRepository.tophighestPriceProduct();

        return transfer(list);
    }

    //Lấy các sản phẩm theo phân trang cho trang shop
    @Override
    public Page<ProductDto> searchCustomerProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(productRepository.searchProducts(keyword));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    //Lấy Các sản phẩm theo loại có phân trang cho trang shop
    @Override
    public Page<ProductDto> searchCusTomerProductsByCategory(int pageNo, Long id) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(productRepository.searchCusTomerProductsByCategory(id));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    //Lấy các sản phẩm theo phân trang sắp xếp từ cao nhất
    @Override
    public Page<ProductDto> getDescProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(productRepository.getDescProducts());
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    //Lấy các sản phẩm theo phân trang sắp xếp từ thấp nhất
    @Override
    public Page<ProductDto> getAscProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoList = transfer(productRepository.getAscProducts());
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    //Tìm sản phẩm theo id
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).get();
    }

    //Xóa vĩnh viễn sản phẩm
    @Override
    public void hardDeleteProduct(Long id) {
        productRepository.hardDeleteProduct(id);
    }

    //Cấu hình trang các sản phẩm

    private Page toPage(List<ProductDto> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size()) ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    //chuyển đổi thành danh sách dto
    private List<ProductDto> transfer(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setImage(product.getImage());
            productDto.set_deleted(product.is_deleted());
            productDto.set_activated(product.is_activated());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
