package com.prm.bookstore.UI.Book;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prm.bookstore.Models.Book.Book;
import com.prm.bookstore.R;
import java.util.List;
import java.util.Locale;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<Book> books;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookListAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvBookName.setText(book.getName());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "%.0fđ", book.getPrice()));
        holder.imgCover.setImageResource(R.drawable.book_1);
        holder.itemView.setBackgroundResource(R.drawable.card_bg);
        holder.itemView.setPadding(24, 24, 24, 24);
        holder.tvBookName.setTextSize(18);
        holder.tvBookName.setTextColor(0xFF212121);
        holder.tvAuthor.setTextColor(0xFF757575);
        holder.tvPrice.setTextColor(0xFF009688);
        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvBookName, tvAuthor, tvPrice;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
