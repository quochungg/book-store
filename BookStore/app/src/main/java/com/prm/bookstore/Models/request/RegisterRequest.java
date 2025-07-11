package com.prm.bookstore.Models.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.net.Uri;

public class RegisterRequest {

    @NonNull
    private String userName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @Nullable
    private String phoneNumber;

    @Nullable
    private Uri avatarImgUri; // Thay cho IFormFile, dùng Uri để chọn file ảnh trên Android

    private int roleID;

    // Constructor mặc định
    public RegisterRequest() {}

    // Constructor đầy đủ
    public RegisterRequest(@NonNull String userName, @NonNull String password, @NonNull String email,
                                  @Nullable String phoneNumber, @Nullable Uri avatarImgUri, int roleID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarImgUri = avatarImgUri;
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
    public Uri getAvatarImgUri() {
        return avatarImgUri;
    }

    public void setAvatarImgUri(@Nullable Uri avatarImgUri) {
        this.avatarImgUri = avatarImgUri;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
