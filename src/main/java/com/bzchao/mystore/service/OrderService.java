package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;

public interface OrderService {
    Order findByOidWithItem(String oid);

    boolean insert(Order order);

    boolean insertOrderItem(OrderItem orderItem);
}
