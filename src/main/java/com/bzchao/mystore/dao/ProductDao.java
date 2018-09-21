package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductDao {

    int getCountAll();

    int getCountByCid(String cid);

    List<Product> findHot();

    List<Product> findByCidLimit(String pid, int start, int count);

    Product findByPid(String pid);
}
