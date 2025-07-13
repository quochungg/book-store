package com.prm.bookstore.UI.Payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.HashMap;
import java.util.Map;

public class PaymentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        EditText edtOrderDescription = view.findViewById(R.id.edtOrderDescription);
        EditText edtName = view.findViewById(R.id.edtName);
        Button btnPay = view.findViewById(R.id.btnPay);
        WebView webView = new WebView(requireContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                if (url.contains("localhost") || url.contains("127.0.0.1")) {
                    // Xóa WebView khỏi ViewGroup cha nếu có
                    ViewGroup parent = (ViewGroup) webView.getParent();
                    if (parent != null) {
                        parent.removeView(webView);
                    }
                    requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, new SuccessPaymentFragment())
                        .addToBackStack(null)
                        .commit();
                    view.stopLoading();
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("vnp_ResponseCode=00") && url.contains("vnp_TransactionStatus=00")) {
                    ViewGroup parent = (ViewGroup) webView.getParent();
                    if (parent != null) {
                        parent.removeView(webView);
                    }
                    requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, new SuccessPaymentFragment())
                        .addToBackStack(null)
                        .commit();
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            edtAmount.setText(args.getString("amount", ""));
            // Không tự động điền mô tả và tên, để người dùng tự nhập
        }

        btnPay.setOnClickListener(v -> {
            String orderType = "other"; // Giá trị mặc định
            String amount = edtAmount.getText().toString().trim();
            String orderDescription = edtOrderDescription.getText().toString().trim();
            String name = edtName.getText().toString().trim();

            if (amount.isEmpty() || orderDescription.isEmpty() || name.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("orderType", orderType);
            body.put("amount", amount);
            body.put("orderDescription", orderDescription);
            body.put("name", name);

            ApiService apiService = ApiClient.getAnonymousClient().create(ApiService.class);
            apiService.payment(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String paymentUrl = response.body().string();
                            // Nhúng web thanh toán vào fragment
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.loadUrl(paymentUrl);
                            ((ViewGroup) requireView().getParent()).removeView(requireView());
                            requireActivity().addContentView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Lỗi xử lý thanh toán", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }
}
