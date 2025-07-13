package com.prm.bookstore.UI.Book;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.Book.Book;
import com.prm.bookstore.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookListAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookListAdapter(bookList, this::onBookClick);
        recyclerView.setAdapter(adapter);

        // Lấy token từ arguments (nếu có)
        token = getArguments() != null ? getArguments().getString("token") : null;

        loadBooks();

        // Gọi API check giỏ hàng nếu có token
        if (token != null) {
            checkCartStatus(token);
        }

        return view;
    }

    private void loadBooks() {
        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList.clear();
                    bookList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onBookClick(Book book) {
        Fragment detailFragment = BookDetailFragment.newInstance(book.getId(), token);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void checkCartStatus(String token) {
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);

        apiService.hasProductInCart().enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    showCartNotification();
                } else {
                    Log.d("BookListFragment", "CheckCart response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("BookListFragment", "CheckCart failed: " + t.getMessage());
            }
        });
    }

    private void showCartNotification() {
        if (!isAdded() || getContext() == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Gọi xin quyền
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 2001);
                return; // Dừng lại, đợi người dùng chọn quyền rồi mới gửi thông báo
            }
        }


        String channelId = "cart_channel_id";
        String channelName = "Cart Notifications";

        NotificationManager notificationManager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo channel cho Android 8.0+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Thông báo giỏ hàng");
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_cart_notification) // Đảm bảo bạn có icon trong drawable
                .setContentTitle("Giỏ hàng")
                .setContentText("Bạn có sản phẩm chưa thanh toán ở giỏ hàng!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Hiển thị thông báo
        notificationManager.notify(1001, builder.build());
    }

}
