package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.OrderItem;

public interface OrderItemDao {
    int insert(OrderItem orderItem);

    OrderItem findByItemIdWithProduct(String ItemId);
}
