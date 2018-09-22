package com.bzchao.mystore.entity;

/**
 * 购物车商品条目
 */
public class CartItem {
   private Product product;
    private int count;
    private double subPrice;

    public double getSubPrice() {
        return product.getShopPrice() * count;
    }

    public void setSubPrice(double totalPrice) {
        this.subPrice = totalPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
