package com.ecommerce.customer.exception;

//Tạo class  NotFoundException
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
