package com.haitao.javawebproject.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long id) {
        super("could not found " +id);
    }
}
