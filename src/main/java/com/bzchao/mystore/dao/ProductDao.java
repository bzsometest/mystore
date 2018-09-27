package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductDao {

    int getCountAll();

    int getCountByCid(String cid);

    int insert(Product product);

    int update(Product product);

    int delete(String pid);

    int push(String pid, int state);

    List<Product> findHot();

    List<Product> findAll();

    List<Product> findByPush(int state);

    List<Product> findByCidLimit(String cid, int start, int count);

    List<Product> findByLimit(int start, int count);

    Product findByPid(String pid);
}
