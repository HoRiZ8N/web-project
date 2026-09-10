package com.inno.webproject.model;

public record User(Long id, String username, String password, String email, String role) {

    public User(String username, String password, String email) {
        this(null, username, password, email, "USER");
    }
}
