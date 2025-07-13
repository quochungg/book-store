package com.prm.bookstore.API;

import com.prm.bookstore.Models.Chat.Message;
import com.prm.bookstore.Models.Chat.SendMessageModel;
import com.prm.bookstore.Models.response.LoginResponse;
import com.prm.bookstore.Models.response.RegisterResponse;
import com.prm.bookstore.Models.request.LoginRequest;
import com.prm.bookstore.Models.Book.Book;
import com.prm.bookstore.Models.request.AddToCartRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("/api/Book/GetAllBook")
    Call<java.util.List<Book>> getAllBooks();

    @GET("/api/Book/GetBookById")
    Call<Book> getBookById(@Query("id") int id);

    @POST("/api/Cart/AddBookToCart")
    Call<ResponseBody> addBookToCart(@Body AddToCartRequest request);

    @GET("/api/Cart/GetUserCart")
    Call<com.prm.bookstore.Models.response.CartViewModel> getUserCart();

    @POST("/api/Payment")
    Call<ResponseBody> payment(@Body Map<String, String> body);

    @DELETE("/api/Cart/RemoveCartDetail")
    Call<ResponseBody> removeCartDetail(@Query("bookid") int bookId);
}
