package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.Order;

import java.util.List;

public interface OrderDao {
    Order findByOidWithAll(String oid);

    List<Order> findByUidWithAll(String uid);

    int insert(Order order);

    int delete(String oid);
}
