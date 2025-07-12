package com.prm.bookstore.Models.Chat;

import com.google.gson.annotations.SerializedName;

public class SendMessageModel {
    @SerializedName("ReceiverId")
    private String receiverId;
    @SerializedName("Content")
    private String content;

    public SendMessageModel(String receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }

    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
}

