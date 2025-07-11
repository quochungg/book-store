package com.prm.bookstore.Models.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("UserName")
    @NonNull
    private String userName;

    @SerializedName("Password")
    @NonNull
    private String password;

    @SerializedName("Email")
    @NonNull
    private String email;

    @SerializedName("PhoneNumber")
    @Nullable
    private String phoneNumber;

    @SerializedName("RoleID")
    private int roleID;

    // Constructor mặc định
    public RegisterRequest() {}

    // Constructor đầy đủ
    public RegisterRequest(@NonNull String userName, @NonNull String password, @NonNull String email,
                                  @Nullable String phoneNumber, int roleID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
    }

    // Getter và Setter
    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
