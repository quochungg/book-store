package com.prm.bookstore.Models.response;

public class CartDetailViewModel {
    private String bookName;
    private int quantity;
    private double total;

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}

