package com.example.androidhttpserver.servlet.exception;



public class NoServletException extends RuntimeException {

    public NoServletException(){}
    public NoServletException(String name){
        super(name);
    }
}
