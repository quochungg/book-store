package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.response.CartViewModel;
import com.prm.bookstore.Models.response.CartDetailViewModel;
import com.prm.bookstore.R;
import com.prm.bookstore.UI.Payment.PaymentFragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvCartEmpty;
    private TextView tvTotalAmount;
    private List<CartDetailViewModel> cartItems = new ArrayList<>();
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString("token");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        tvCartEmpty = view.findViewById(R.id.tvCartEmpty);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        cartAdapter = new CartAdapter(cartItems, this, token);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCart.setAdapter(cartAdapter);
        loadCart();
        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            // Tính tổng tiền
            double totalAmount = 0;
            StringBuilder descriptionBuilder = new StringBuilder();
            String name = "";
            for (CartDetailViewModel item : cartItems) {
                totalAmount += item.getTotal();
                descriptionBuilder.append(item.getBookName()).append(", ");
            }
            if (!cartItems.isEmpty()) {
                name = cartItems.get(0).getBookName(); // hoặc lấy tên người dùng nếu có
            }
            String orderDescription = descriptionBuilder.toString();
            // Chuyển sang PaymentFragment, truyền dữ liệu
            Bundle bundle = new Bundle();
            bundle.putString("orderType", "other");
            bundle.putString("amount", String.valueOf((int)totalAmount)); // Số tiền sẽ được lấy từ tổng tiền giỏ hàng
            bundle.putString("orderDescription", orderDescription);
            bundle.putString("name", name);
            PaymentFragment paymentFragment = new PaymentFragment();
            paymentFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_fragment_container, paymentFragment)
                .addToBackStack(null)
                .commit();
        });
        return view;
    }

    public void loadCart() {
        if (token == null) {
            tvCartEmpty.setText("Bạn cần đăng nhập để xem giỏ hàng");
            tvCartEmpty.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
            return;
        }
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);
        apiService.getUserCart().enqueue(new Callback<CartViewModel>() {
            @Override
            public void onResponse(Call<CartViewModel> call, Response<CartViewModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCartDetails() != null && !response.body().getCartDetails().isEmpty()) {
                    cartItems.clear();
                    cartItems.addAll(response.body().getCartDetails());
                    cartAdapter.notifyDataSetChanged();
                    tvCartEmpty.setVisibility(View.GONE);
                    recyclerViewCart.setVisibility(View.VISIBLE);
                    // Tính tổng tiền và hiển thị lên giao diện
                    double totalAmount = 0;
                    for (CartDetailViewModel item : cartItems) {
                        totalAmount += item.getTotal();
                    }
                    tvTotalAmount.setText("Tổng tiền: " + String.format("%.0fđ", totalAmount));
                } else {
                    tvCartEmpty.setText("Giỏ hàng trống");
                    tvCartEmpty.setVisibility(View.VISIBLE);
                    recyclerViewCart.setVisibility(View.GONE);
                    tvTotalAmount.setText("");
                }
            }
            @Override
            public void onFailure(Call<CartViewModel> call, Throwable t) {
                Log.e("CartFragment", "Lỗi lấy giỏ hàng: " + t.getMessage(), t);
                tvCartEmpty.setText("Lỗi kết nối: " + t.getMessage());
                tvCartEmpty.setVisibility(View.VISIBLE);
                recyclerViewCart.setVisibility(View.GONE);
            }
        });
    }
}
