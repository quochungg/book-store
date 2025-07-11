package com.prm.bookstore.Models.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisterResponse {
    @NonNull
    private String userName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String avatarImgUrl;

    private int roleID;

    // Constructor mặc định
    public RegisterResponse() {}

    // Constructor đầy đủ
    public RegisterResponse(@NonNull String userName, @NonNull String password, @NonNull String email,
                               @Nullable String phoneNumber, @Nullable String avatarImgUrl, int roleID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarImgUrl = avatarImgUrl;
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

    @Nullable
    public String getAvatarImgUrl() {
        return avatarImgUrl;
    }

    public void setAvatarImgUrl(@Nullable String avatarImgUrl) {
        this.avatarImgUrl = avatarImgUrl;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
