package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    CustomerRepository customerRepository;

    //Lưu order
    @Override
    public void saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus("PENDING");
        order.setCustomer(shoppingCart.getCustomer());
        order.setTotalPrice(shoppingCart.getTotalPrices());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItem()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setProduct(item.getProduct());
            orderDetail.setUnitPrice(item.getProduct().getSalePrice());
            orderRepository.save(order);
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
            cartItemRepository.deleteItem(item.getId());
        }
        order.setOrderDetailList(orderDetailList);
        shoppingCart.setCartItem(new HashSet<>());
        shoppingCart.setTotalItems(0);
        shoppingCart.setTotalPrices(0);
        shoppingCartRepository.save(shoppingCart);
        orderRepository.save(order);
    }

    //chấp nhậ order dành cho admin
    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setDeliveryDate(new Date());
        order.setOrderStatus("SHIPPING");
        orderRepository.save(order);
    }
    //undo hành động accept
    @Override
    public void undoAcceptOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setDeliveryDate(null);
        order.setOrderStatus("PENDING");
        orderRepository.save(order);
    }

    //xóa order
    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    //Lấy page All orders
    @Override
    public Page<Order> getAllOrder(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<Order> orders = orderRepository.findAll();
        Page<Order> orderPages = toPage(orders, pageable);
        return orderPages;
    }

    @Override
    public void updateNote(Long id,String notes) {
        Order order=orderRepository.findById(id).get();
        order.setNotes(notes);
        orderRepository.save(order);
    }

    //lấy sublist theo index
    private Page toPage(List<Order> list, Pageable pageable) {
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
