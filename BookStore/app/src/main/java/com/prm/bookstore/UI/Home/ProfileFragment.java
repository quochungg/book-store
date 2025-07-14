package com.prm.bookstore.UI.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.User.UserInfoResponse;
import com.prm.bookstore.R;
import com.prm.bookstore.UI.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvUserId, tvUserRole;
    private View btnDirections, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        loadUserInfo();
        setupClickListeners();
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvUserId = view.findViewById(R.id.tvUserId);
        tvUserRole = view.findViewById(R.id.tvUserRole);
        btnDirections = view.findViewById(R.id.btnDirections);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void loadUserInfo() {
        // Lấy thông tin user từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", 0);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("userId", "");
        
        // Lấy thông tin từ Intent (nếu có)
        if (getArguments() != null) {
            String intentUserId = getArguments().getString("userId", "");
            if (!intentUserId.isEmpty()) {
                userId = intentUserId;
            }
        }

        // Hiển thị thông tin user cơ bản
        if (!userId.isEmpty()) {
            tvUserId.setText(userId);
        }
        
        // Gọi API để lấy thông tin chi tiết user
        if (!token.isEmpty()) {
            fetchUserInfo(token);
        } else {
            // Hiển thị thông tin mặc định nếu không có token
            showDefaultUserInfo();
        }
    }

    private void showDefaultUserInfo() {
        tvUsername.setText("Người dùng");
        tvEmail.setText("user@example.com");
        tvUserId.setText("Chưa cập nhật");
        tvUserRole.setText("Chưa cập nhật");
    }

    private void fetchUserInfo(String token) {
        Retrofit retrofit = ApiClient.getAuthenticatedClient(token);
        if (retrofit == null) {
            Log.e("ProfileFragment", "Failed to create authenticated client");
            showDefaultUserInfo();
            return;
        }
        
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserInfoResponse> call = apiService.getUserInfo();
        
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserInfoResponse userInfo = response.body();
                    if (userInfo.isSuccess() && userInfo.getData() != null) {
                        updateUserInfo(userInfo.getData());
                    } else {
                        showDefaultUserInfo();
                    }
                } else {
                    // Nếu API không thành công, hiển thị thông tin mặc định
                    showDefaultUserInfo();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e("ProfileFragment", "Failed to fetch user info: " + t.getMessage());
                // Hiển thị thông tin mặc định khi có lỗi
                showDefaultUserInfo();
            }
        });
    }

    private void updateUserInfo(UserInfoResponse.UserInfoData userData) {
        tvUsername.setText(userData.getUserName());
        tvEmail.setText(userData.getEmail());
        
        // Hiển thị số điện thoại nếu có
        String phoneText = userData.getPhoneNumber() != null ? userData.getPhoneNumber() : "Chưa cập nhật";
        
        // Hiển thị địa chỉ nếu có
        String addressText = userData.getAddress() != null ? userData.getAddress() : "Chưa cập nhật";
        
        // Cập nhật layout để hiển thị thêm thông tin
        tvUserId.setText("SĐT: " + phoneText);
        tvUserRole.setText("Địa chỉ: " + addressText);
    }

    private void setupClickListeners() {
        btnDirections.setOnClickListener(v -> openGoogleMaps());
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void openGoogleMaps() {
        // Dùng tên địa điểm (phải encode thành chuỗi URL hợp lệ, dấu cách thành + hoặc %20)
        String locationName = "Nhà sách FAHASA Tân Định, TP HCM";
        String uri = "google.navigation:q=" + Uri.encode(locationName);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Nếu không có Google Maps, fallback sang trình duyệt
            String fallbackUri = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(locationName);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUri)));
        }
    }

    private void handleLogout() {
        // Hiển thị dialog xác nhận đăng xuất
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void performLogout() {
        // Xóa thông tin đăng nhập từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Hiển thị thông báo
        Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
