package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.prm.bookstore.R;

public class MapFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        root.findViewById(R.id.btnOpenGoogleMap).setOnClickListener(v -> {
            String shopAddress = "Nhà sách Fahasa Tân Định, 387-389 Hai Bà Trưng, Quận 3, TP. Hồ Chí Minh, Việt Nam";
            String uri = "geo:0,0?q=" + shopAddress;
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
        return root;
    }
}
