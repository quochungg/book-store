package com.prm.bookstore.UI.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.request.LoginRequest;
import com.prm.bookstore.Models.response.LoginResponse;
import com.prm.bookstore.R;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.UI.Home.HomeActivity;
import com.prm.bookstore.UI.register.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> handleLogin());

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(loginRequest);

        Log.d("LoginActivity", "Sending login request: " + loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("LoginActivity", "Response received: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse data = response.body();
                    String token = data.getData() != null ? data.getData().getToken() : null;
                    // Lưu token vào SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    prefs.edit().putString("token", token).apply();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("storeId", "c7dd2b3e-1e7c-49c4-ced5-08ddc07ce11b"); // Đúng ID admin
                    intent.putExtra("userId", data.getData() != null ? data.getData().getId() : "");
                    Log.d("LoginActivity", "Token put to Intent: " + (data.getData() != null ? data.getData().getToken() : null));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Login failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
