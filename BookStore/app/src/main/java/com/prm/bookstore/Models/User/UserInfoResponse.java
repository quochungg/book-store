package com.prm.bookstore.Models.User;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private UserInfoData data;

    public static class UserInfoData {
        @SerializedName("userName")
        private String userName;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("phoneNumber")
        private String phoneNumber;
        
        @SerializedName("address")
        private String address;
        
        @SerializedName("imgUrl")
        private String imgUrl;

        // Getters
        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        // Setters
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserInfoData getData() {
        return data;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }
} 