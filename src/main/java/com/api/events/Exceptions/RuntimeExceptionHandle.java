package com.api.events.Exceptions;

public class RuntimeExceptionHandle extends Exception {
    public RuntimeExceptionHandle(String error_with_user_password) {
        System.out.println(error_with_user_password+"****************************** ");
    }
}
