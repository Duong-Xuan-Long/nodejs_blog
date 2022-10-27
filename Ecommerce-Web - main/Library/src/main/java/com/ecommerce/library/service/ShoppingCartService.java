package com.ecommerce.library.service;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;

public interface ShoppingCartService {
    //Thêm item vào cart
    ShoppingCart addItemtoCart(Product product, int quantity, Customer customer);

    //update shopping cart
    ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

    //Xóa item ở shopping cart
    ShoppingCart deleteItemInCart(Product product, Customer customer);
}
