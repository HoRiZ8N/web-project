package com.inno.webproject.util;

public class DatabasePoolException extends Exception {

    public DatabasePoolException(String message) {
        super(message);
    }

    public DatabasePoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
