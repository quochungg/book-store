package com.prm.bookstore.Models.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName(value = "success", alternate = {"Success"})
    private boolean success;

    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
