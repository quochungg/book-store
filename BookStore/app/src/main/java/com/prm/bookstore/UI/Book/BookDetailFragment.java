package com.prm.bookstore.UI.Book;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.Book.Book;
import com.prm.bookstore.R;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailFragment extends Fragment {
    private static final String ARG_BOOK_ID = "book_id";
    private int bookId;
    private String token;

    public static BookDetailFragment newInstance(int bookId, String token) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOK_ID, bookId);
        args.putString("token", token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getInt(ARG_BOOK_ID);
            token = getArguments().getString("token");
        }
        // Nếu token vẫn null, lấy từ SharedPreferences
        if (token == null && getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE);
            token = prefs.getString("token", null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        ImageView imgCover = view.findViewById(R.id.imgCover);
        TextView tvBookName = view.findViewById(R.id.tvBookName);
        TextView tvAuthor = view.findViewById(R.id.tvAuthor);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvPreContent = view.findViewById(R.id.tvPreContent);
        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);
        loadBookDetail(imgCover, tvBookName, tvAuthor, tvPrice, tvPreContent);
        btnAddToCart.setOnClickListener(v -> {
            addBookToCart(bookId, 1); // Default quantity is 1
        });
        return view;
    }

    private void loadBookDetail(ImageView imgCover, TextView tvBookName, TextView tvAuthor, TextView tvPrice, TextView tvPreContent) {
        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        apiService.getBookById(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    tvBookName.setText(book.getName());
                    tvAuthor.setText(book.getAuthor());
                    tvPrice.setText(String.format(Locale.getDefault(), "%.0fđ", book.getPrice()));
                    tvPreContent.setText(book.getPreContent());
                    // Nếu có ảnh bìa là URL, có thể dùng Glide/Picasso, tạm thời dùng ảnh mặc định
                    imgCover.setImageResource(R.drawable.book_1);
                } else {
                    Toast.makeText(getContext(), "Không tải được chi tiết sách", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookToCart(int bookId, int quantity) {
        Log.d("BookDetailFragment", "Token: " + token);
        if (token == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);
        com.prm.bookstore.Models.request.AddToCartRequest request = new com.prm.bookstore.Models.request.AddToCartRequest(bookId, quantity);
        apiService.addBookToCart(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("BookDetailFragment", "AddToCart response: " + response.code());
                if (response.isSuccessful()) {
                    String body = "";
                    try {
                        body = response.body() != null ? response.body().string() : "";
                    } catch (Exception e) {
                        Log.e("BookDetailFragment", "Error reading response body", e);
                    }
                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    Log.d("BookDetailFragment", "AddToCart body: " + body);
                } else {
                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("BookDetailFragment", "AddToCart error: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
