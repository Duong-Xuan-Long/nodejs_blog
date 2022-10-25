package com.ecommerce.admin.exception;

//Tạo class BadRequestException
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
