package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;

import java.util.List;

public interface OrderService {
    List<Order> findByUidWithAll(String uid);

    Order findByOidWithAll(String oid);

    boolean insert(Order order);

    boolean insertOrderItem(OrderItem orderItem);
}
