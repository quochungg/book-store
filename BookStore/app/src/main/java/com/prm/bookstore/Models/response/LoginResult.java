package com.prm.bookstore.Models.response;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("role")
    private int role;
    @SerializedName("token")
    private String token;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

