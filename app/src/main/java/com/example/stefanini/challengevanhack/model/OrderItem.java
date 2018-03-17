package com.example.stefanini.challengevanhack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class OrderItem {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("orderId")
    @Expose
    private int orderId;
    @SerializedName("productId")
    @Expose
    private int productId;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("total")
    @Expose
    private int total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
