package com.prm.bookstore.Models.response;

import com.google.gson.annotations.SerializedName;

public class CartDetailViewModel {
    @SerializedName("bookName")
    private String bookName;
    @SerializedName("quatity")
    private int quantity;
    @SerializedName("total")
    private double total;
    @SerializedName("bookId")
    private int bookId;

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
}
