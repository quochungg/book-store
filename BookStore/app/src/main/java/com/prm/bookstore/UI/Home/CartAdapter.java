package com.prm.bookstore.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prm.bookstore.Models.response.CartDetailViewModel;
import com.prm.bookstore.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartDetailViewModel> cartItems;

    public CartAdapter(List<CartDetailViewModel> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_detail, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartDetailViewModel item = cartItems.get(position);
        holder.tvBookName.setText(item.getBookName());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvTotal.setText("Tổng: " + String.format("%.0fđ", item.getTotal()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvQuantity, tvTotal;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}

