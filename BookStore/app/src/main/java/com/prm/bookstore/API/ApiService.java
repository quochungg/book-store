package com.prm.bookstore.API;

import com.prm.bookstore.Models.request.LoginRequest;
import com.prm.bookstore.Models.request.RegisterRequest;
import com.prm.bookstore.Models.response.LoginResponse;
import com.prm.bookstore.Models.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/Account/Login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("/api/Account/Register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
