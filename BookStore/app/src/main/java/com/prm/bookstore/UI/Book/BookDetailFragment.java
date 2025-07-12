package com.prm.bookstore.UI.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailFragment extends Fragment {
    private static final String ARG_BOOK_ID = "book_id";
    private int bookId;

    public static BookDetailFragment newInstance(int bookId) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getInt(ARG_BOOK_ID);
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
        loadBookDetail(imgCover, tvBookName, tvAuthor, tvPrice, tvPreContent);
        return view;
    }

    private void loadBookDetail(ImageView imgCover, TextView tvBookName, TextView tvAuthor, TextView tvPrice, TextView tvPreContent) {
        ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
        apiService.getBookById(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    tvBookName.setText(book.getName());
                    tvAuthor.setText(book.getAuthor());
                    tvPrice.setText(String.format("%.0fđ", book.getPrice()));
                    tvPreContent.setText(book.getPreContent());
                    // Nếu có ảnh bìa là URL, có thể dùng Glide/Picasso, tạm thời dùng ảnh mặc định
                    imgCover.setImageResource(R.drawable.book_1);
                } else {
                    Toast.makeText(getContext(), "Không tải được chi tiết sách", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
