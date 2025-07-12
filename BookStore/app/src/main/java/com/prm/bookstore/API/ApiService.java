package com.prm.bookstore.API;

import com.prm.bookstore.Models.Chat.Message;
import com.prm.bookstore.Models.Chat.SendMessageModel;
import com.prm.bookstore.Models.response.LoginResponse;
import com.prm.bookstore.Models.response.RegisterResponse;
import com.prm.bookstore.Models.request.LoginRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/Account/Login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @Multipart
    @POST("/api/Account/Register")
    Call<RegisterResponse> register(
        @Part("UserName") RequestBody userName,
        @Part("Password") RequestBody password,
        @Part("Email") RequestBody email,
        @Part("PhoneNumber") RequestBody phoneNumber,
        @Part MultipartBody.Part avatarImg,
        @Part("RoleID") RequestBody roleID
    );

    @POST("/api/Chat/SendMessage")
    Call<String> sendMessage(@Body SendMessageModel message);

    @GET("/api/Chat/GetMessages")
    Call<List<Message>> getMessages(@Query("userId") String userId, @Query("storeId") String storeId);

    @GET("/api/Chat/GetUserChat")
    Call<List<Message>> getUserChat();
}
