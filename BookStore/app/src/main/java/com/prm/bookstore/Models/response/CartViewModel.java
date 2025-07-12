package com.prm.bookstore.Models.response;
import java.util.List;

public class CartViewModel {
    private int id;
    private List<CartDetailViewModel> cartDetails;

    public CartViewModel() {}

    public CartViewModel(int id, List<CartDetailViewModel> cartDetails) {
        this.id = id;
        this.cartDetails = cartDetails;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public List<CartDetailViewModel> getCartDetails() { return cartDetails; }
    public void setCartDetails(List<CartDetailViewModel> cartDetails) { this.cartDetails = cartDetails; }
}
