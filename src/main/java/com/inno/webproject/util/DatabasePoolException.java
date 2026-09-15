package com.inno.webproject.util;

import java.sql.SQLException;

public class DatabasePoolException extends SQLException {

    public DatabasePoolException(String message) {
        super(message);
    }

    public DatabasePoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
