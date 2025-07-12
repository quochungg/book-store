package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.prm.bookstore.API.ApiClient;
import com.prm.bookstore.API.ApiService;
import com.prm.bookstore.Models.Chat.Message;
import com.prm.bookstore.Models.Chat.SendMessageModel;
import com.prm.bookstore.R;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String userId = "customer_id"; // TODO: Get from logged-in user
    private String storeId = "store_id";   // TODO: Set store id
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity().getIntent() != null) {
            token = getActivity().getIntent().getStringExtra("token");
            android.util.Log.d("ChatFragment", "Token: " + token);
            // Nhận storeId từ Intent nếu có, nếu không thì dùng mặc định
            String intentStoreId = getActivity().getIntent().getStringExtra("storeId");
            storeId = intentStoreId != null ? intentStoreId : "admin-id-123"; // Thay bằng ID thật của cửa hàng/admin
            String intentUserId = getActivity().getIntent().getStringExtra("userId");
            userId = intentUserId != null ? intentUserId : "customer_id";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> sendMessage());
        loadMessages();
        return view;
    }

    private void loadMessages() {
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);
        // Đổi sang endpoint GetUserChat, không cần truyền userId/storeId
        apiService.getUserChat().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {}
        });
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;
        SendMessageModel message = new SendMessageModel(storeId, content);
        ApiService apiService = ApiClient.getAuthenticatedClient(token).create(ApiService.class);
        apiService.sendMessage(message).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    loadMessages();
                    etMessage.setText("");
                } else {
                    // Log lỗi chi tiết
                    String errorMsg = "";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg = e.getMessage();
                        }
                    }
                    android.util.Log.e("ChatFragment", "SendMessage failed: code=" + response.code() + ", msg=" + errorMsg);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                android.util.Log.e("ChatFragment", "SendMessage error: " + t.getMessage());
            }
        });
    }
}
