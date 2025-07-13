package com.prm.bookstore.UI.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Tạo layout cha
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);

        // Text tiêu đề
        TextView tv = new TextView(getContext());
        tv.setText("Profile Fragment");
        tv.setTextSize(24);
        layout.addView(tv);

        // Tạo Button mở Google Maps
        Button mapButton = new Button(getContext());
        mapButton.setText("Chỉ đường đến cửa hàng");
        layout.addView(mapButton);

        // Xử lý sự kiện click
        mapButton.setOnClickListener(v -> openGoogleMaps());

        return layout;
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

}
