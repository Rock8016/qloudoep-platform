package com.hrjk.fin.demo.exception;

public class BusinessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String msg, Throwable e){
        super(msg,e);
    }

}
