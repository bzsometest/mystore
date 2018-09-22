package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;

import java.util.List;

public interface OrderService {
    List<Order> findByUidWithAll(String uid);

    Order findByOidWithAll(String oid);

    Order findByOidSimple(String oid);

    Order insert(Order order);

    boolean update(Order order);

    boolean delete(String oid);

    /**
     * 在数据库中创建商品条目列表
     */
    void addOrderItem(List<OrderItem> orderItemList, String oid);
}
