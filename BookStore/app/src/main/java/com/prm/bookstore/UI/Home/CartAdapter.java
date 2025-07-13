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
    private CartFragment cartFragment;
    private String token;

    public CartAdapter(List<CartDetailViewModel> cartItems, CartFragment cartFragment, String token) {
        this.cartItems = cartItems;
        this.cartFragment = cartFragment;
        this.token = token;
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

    private void updateCart(CartDetailViewModel item, int newQuantity) {
        if (token == null) return;
        com.prm.bookstore.API.ApiService apiService = com.prm.bookstore.API.ApiClient.getAuthenticatedClient(token).create(com.prm.bookstore.API.ApiService.class);
        com.prm.bookstore.Models.request.AddToCartRequest request = new com.prm.bookstore.Models.request.AddToCartRequest(item.getBookId(), newQuantity);
        android.util.Log.d("CartAdapter", "UpdateCart request: bookId=" + item.getBookId() + ", quantity=" + newQuantity);
        apiService.addBookToCart(request).enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Log response để kiểm tra
                    try {
                        String body = response.body() != null ? response.body().string() : "";
                        android.util.Log.d("CartAdapter", "UpdateCart success: " + body);
                    } catch (Exception e) {
                        android.util.Log.e("CartAdapter", "Error reading response body", e);
                    }
                } else {
                    android.util.Log.e("CartAdapter", "UpdateCart failed: " + response.message());
                }
                if (cartFragment != null) cartFragment.loadCart();
            }
            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                android.util.Log.e("CartAdapter", "UpdateCart error: " + t.getMessage(), t);
            }
        });
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
