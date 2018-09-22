package com.bzchao.mystore.entity;

import java.util.List;

/**
 * 用户购物车
 */
public class Cart {
    private List<CartItem> cartItemList;
    private double totalPrice;

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public double getTotalPrice() {
        totalPrice = 0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getSubPrice();
        }
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
