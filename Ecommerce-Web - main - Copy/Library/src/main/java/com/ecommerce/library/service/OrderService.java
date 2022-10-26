package com.ecommerce.library.service;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import org.springframework.data.domain.Page;

public interface OrderService {
    //Lưu order
    void saveOrder(ShoppingCart shoppingCart);

    //Chấp nhận order
    void acceptOrder(Long id);
    //undo accepting order
    void undoAcceptOrder(Long id);

    //Xóa order
    void cancelOrder(Long id);

    //Lấy tất cả order
    Page<Order> getAllOrder(int pageNo);

    void updateNote(Long id,String notes);
}
