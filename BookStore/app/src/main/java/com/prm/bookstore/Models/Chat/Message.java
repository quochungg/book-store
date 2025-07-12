package com.prm.bookstore.Models.Chat;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private String id;
    @SerializedName("senderId")
    private String senderId;
    @SerializedName("receiverId")
    private String receiverId;
    @SerializedName("content")
    private String content;
    @SerializedName("timestamp")
    private String timestamp;

    public Message(String id, String senderId, String receiverId, String content, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
}

