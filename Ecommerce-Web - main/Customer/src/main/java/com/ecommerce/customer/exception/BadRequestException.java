package com.ecommerce.customer.exception;

//Tạo class BadRequestException
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
