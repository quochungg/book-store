package com.prm.bookstore.UI.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.response.RegisterResponse;
import com.prm.bookstore.R;
import com.prm.bookstore.UI.login.LoginActivity;
import com.prm.bookstore.API.ApiClient;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etEmail, etPhoneNumber;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        int roleID = 1; // Mặc định Customer

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo RequestBody
        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phoneNumber.isEmpty() ? "" : phoneNumber);
        RequestBody roleIDBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(roleID));

        // Tạo file rỗng cho AvatarImg
        MultipartBody.Part avatarImgPart = MultipartBody.Part.createFormData(
                "AvatarImg",
                "empty.png",
                RequestBody.create(MediaType.parse("application/octet-stream"), new byte[0])
        );

        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        Call<RegisterResponse> call = apiService.register(
                userNameBody,
                passwordBody,
                emailBody,
                phoneBody,
                avatarImgPart,
                roleIDBody
        );

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("DEBUG", "HTTP Code: " + response.code());
                if (response.isSuccessful()) {
                    RegisterResponse res = response.body();
                    if (res != null) {
                        Log.d("DEBUG", "Success: " + res.isSuccess() + ", Message: " + res.getMessage());
                        if (res.isSuccess()) {
                            Toast.makeText(RegisterActivity.this, res.getMessage() != null ? res.getMessage() : "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, res.getMessage() != null ? res.getMessage() : "Đăng ký thất bại!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi: server trả về dữ liệu rỗng!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String errorMsg = "Đăng ký thất bại! Mã lỗi: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("DEBUG", "Error parsing errorBody", e);
                        }
                    }
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "onFailure", t);
            }
        });
    }
}
