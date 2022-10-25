package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import com.ecommerce.library.utils.RoundDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private RoundDouble roundDouble;

    //THêm item vào cart
    @Override
    public ShoppingCart addItemtoCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
            shoppingCartRepository.save(cart);
        }

        Set<CartItem> cartItemSet = cart.getCartItem();
        CartItem cartItem = findCartIterm(cartItemSet, product.getId());
        if (cartItemSet == null) {
            cartItemSet = new HashSet<>();
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setTotalPrice(quantity * product.getSalePrice());
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItemSet.add(cartItem);
            cartItemRepository.save(cartItem);

        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getSalePrice());
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItemSet.add(cartItem);
                cartItemRepository.save(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice() + quantity * product.getSalePrice());
            }
        }
        cart.setCartItem(cartItemSet);
        int totalItems = totalItems(cart.getCartItem());
        double totalPrice = totalPrice(cart.getCartItem());

        cart.setTotalPrices(totalPrice);
        cart.setTotalItems(totalItems);
        cart.setCustomer(customer);
        return shoppingCartRepository.save(cart);
    }

    //update item của cart
    @Override
    public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItemSet = cart.getCartItem();

        CartItem cartItem = findCartIterm(cartItemSet, product.getId());
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * product.getSalePrice());
        cartItemRepository.save(cartItem);

        int totalItems = totalItems(cartItemSet);
        double totalPrice = totalPrice(cartItemSet);

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);
        return shoppingCartRepository.save(cart);
    }

    //Xóa item khỏi cart
    @Override
    public ShoppingCart deleteItemInCart(Product product, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem item = findCartIterm(cartItems, product.getId());

        cartItems.remove(item);

        cartItemRepository.delete(item);

        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItems(cartItems);

        cart.setCartItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);

        return shoppingCartRepository.save(cart);
    }

    //Tìm các cart item
    private CartItem findCartIterm(Set<CartItem> cartItemSet, Long productId) {
        if (cartItemSet == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItemSet) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    //Tính tổng item
    private int totalItems(Set<CartItem> cartItemSet) {
        int totalItems = 0;
        for (CartItem item : cartItemSet) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    //Tính tổng giá
    private double totalPrice(Set<CartItem> cartItemSet) {
        double totalPrice = 0;
        for (CartItem item : cartItemSet) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
