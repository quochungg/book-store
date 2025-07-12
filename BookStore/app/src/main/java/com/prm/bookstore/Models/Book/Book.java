package com.prm.bookstore.Models.Book;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Book {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("author")
    private String author;
    @SerializedName("coverImg")
    private String coverImg;
    @SerializedName("preContent")
    private String preContent;
    @SerializedName("price")
    private double price;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("bookCates")
    private List<Object> bookCates;

    public Book(int id, String name, String author, String coverImg, String preContent, double price, int quantity, List<Object> bookCates) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.coverImg = coverImg;
        this.preContent = preContent;
        this.price = price;
        this.quantity = quantity;
        this.bookCates = bookCates;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCoverImg() { return coverImg; }
    public void setCoverImg(String coverImg) { this.coverImg = coverImg; }
    public String getPreContent() { return preContent; }
    public void setPreContent(String preContent) { this.preContent = preContent; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public List<Object> getBookCates() { return bookCates; }
    public void setBookCates(List<Object> bookCates) { this.bookCates = bookCates; }
} 