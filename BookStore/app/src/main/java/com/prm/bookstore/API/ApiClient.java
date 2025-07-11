package com.prm.bookstore.API;

import android.util.Log;

import com.prm.bookstore.utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://10.0.2.2:7159/"; // Đúng với API local của bạn
    private static Retrofit retrofit;

    private static OkHttpClient getClient() {
        if (BASE_URL.startsWith("https")) {
            return UnsafeOkHttpClient.getUnsafeOkHttpClient();
        } else {
            return new OkHttpClient.Builder().build();
        }
    }

    public static Retrofit getAnonymousClient() {
        if (retrofit == null) {
            try {
                OkHttpClient client = getClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e) {
                Log.e("ApiClient", "Error creating anonymous client: " + e.getMessage());
            }
        }
        return retrofit;
    }

    public static Retrofit getAuthenticatedClient(String token) {
        try {
            OkHttpClient.Builder clientBuilder = getClient().newBuilder();
            if (token != null) {
                clientBuilder.addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request modified = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .method(originalRequest.method(), originalRequest.body())
                            .build();
                    return chain.proceed(modified);
                });
            }
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            Log.e("ApiClient", "Error creating authenticated client: " + e.getMessage());
            return null;
        }
    }
}
