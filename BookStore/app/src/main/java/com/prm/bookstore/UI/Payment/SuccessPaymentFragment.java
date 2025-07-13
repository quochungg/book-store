package com.prm.bookstore.UI.Payment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.response.CartDetailViewModel;
import com.prm.bookstore.Models.response.CartViewModel;
import com.prm.bookstore.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessPaymentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_payment, container, false);
        TextView tvSuccess = view.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thanh toán thành công!");

        // Lấy token từ SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        Log.d("SuccessPaymentFragment", "Token: " + token);
        if (token == null) {
            Toast.makeText(getContext(), "Không tìm thấy token, vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return view;
        }
        // Xóa giỏ hàng sau khi thanh toán thành công
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);
        apiService.getUserCart().enqueue(new Callback<CartViewModel>() {
            @Override
            public void onResponse(Call<CartViewModel> call, Response<CartViewModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartViewModel cart = response.body();
                    if (cart.getCartDetails() != null && !cart.getCartDetails().isEmpty()) {
                        final StringBuilder resultBuilder = new StringBuilder();
                        final int[] count = {0};
                        final int total = cart.getCartDetails().size();
                        for (CartDetailViewModel item : cart.getCartDetails()) {
                            Log.d("SuccessPaymentFragment", "Xóa bookId: " + item.getBookId() + ", bookName: " + item.getBookName());
                            apiService.removeCartDetail(item.getBookId()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    String serverMessage = "";
                                    try {
                                        if (response.errorBody() != null) {
                                            serverMessage = response.errorBody().string();
                                        }
                                    } catch (Exception e) {}
                                    if (response.isSuccessful()) {
                                        resultBuilder.append("Đã xóa: ").append(item.getBookName()).append("\n");
                                    } else {
                                        resultBuilder.append("Xóa thất bại: ").append(item.getBookName()).append(" (status: ").append(response.code()).append(", message: ").append(serverMessage).append(")\n");
                                    }
                                    count[0]++;
                                    if (count[0] == total) {
                                        Toast.makeText(getContext(), resultBuilder.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    resultBuilder.append("Lỗi xóa: ").append(item.getBookName()).append(" (" + t.getMessage() + ")\n");
                                    count[0]++;
                                    if (count[0] == total) {
                                        Toast.makeText(getContext(), resultBuilder.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Giỏ hàng trống hoặc không có dữ liệu.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không lấy được giỏ hàng (status: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CartViewModel> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi lấy giỏ hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
