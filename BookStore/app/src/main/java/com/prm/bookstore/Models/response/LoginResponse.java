package com.prm.bookstore.Models.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private LoginResult data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LoginResult getData() { return data; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(LoginResult data) { this.data = data; }
}
