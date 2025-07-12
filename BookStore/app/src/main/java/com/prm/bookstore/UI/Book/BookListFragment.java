package com.prm.bookstore.UI.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookListAdapter(bookList, this::onBookClick);
        recyclerView.setAdapter(adapter);
        loadBooks();
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
        // Chuyển sang BookDetailFragment
        Fragment detailFragment = BookDetailFragment.newInstance(book.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.home_fragment_container, detailFragment)
            .addToBackStack(null)
            .commit();
    }
}
