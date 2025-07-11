package com.prm.bookstore.UI.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.request.RegisterRequest;
import com.prm.bookstore.Models.response.RegisterResponse;
import com.prm.bookstore.R;
import com.prm.bookstore.UI.login.LoginActivity;
import com.prm.bookstore.API.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etEmail, etPhoneNumber, etAvatarUrl;
    private RadioGroup rgRole;
    private RadioButton rbCustomer, rbAdmin;
    private Button btnRegister;
    private ImageView imgAvatar;
    private Uri avatarUri = null;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        rgRole = findViewById(R.id.rgRole);
        rbCustomer = findViewById(R.id.rbCustomer);
        rbAdmin = findViewById(R.id.rbAdmin);
        btnRegister = findViewById(R.id.btnRegister);
        imgAvatar = findViewById(R.id.imgAvatar);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        avatarUri = result.getData().getData();
                        imgAvatar.setImageURI(avatarUri);
                    }
                });

        imgAvatar.setOnClickListener(v -> openGallery());
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void handleRegister() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String avatarImgUrl = etAvatarUrl.getText().toString().trim();  // Người dùng nhập URL ảnh hoặc sau khi upload
        int roleID = rbCustomer.isChecked() ? 1 : 2;

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                userName,
                password,
                email,
                phoneNumber.isEmpty() ? null : phoneNumber,
                avatarImgUrl.isEmpty() ? null : avatarUri,
                roleID
        );

        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        Call<RegisterResponse> call = apiService.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
