package com.bzchao.mystore.entity;

import java.util.Map;

/**
 * 用户购物车
 */
public class Cart {
    private Map<String, CartItem> cartItemMap;
    private double totalPrice;

    public Map<String, CartItem> getCartItemMap() {
        return cartItemMap;
    }

    public void setCartItemMap(Map<String, CartItem> cartItemMap) {
        this.cartItemMap = cartItemMap;
    }

    public double getTotalPrice() {
        totalPrice = 0;
        for (Map.Entry<String, CartItem> entry : cartItemMap.entrySet()) {
            totalPrice += entry.getValue().getSubPrice();
        }
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
