package com.prm.bookstore.Models.User;

import androidx.annotation.NonNull;

public class UserResponse {
    @NonNull
    private String id;  // Guid kiểu string trong Java

    @NonNull
    private String username;

    private int role;

    @NonNull
    private String token;

    // Constructor mặc định
    public UserResponse() {}

    // Constructor đầy đủ
    public UserResponse(@NonNull String id, @NonNull String username, int role, @NonNull String token) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.token = token;
    }

    // Getter và Setter
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }
}
