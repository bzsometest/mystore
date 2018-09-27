package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductService {

    int getCountAll();

    int getCountByCid(String cid);

    boolean insert(Product product);

    boolean delete(String pid);

    boolean pushDown(String pid);

    boolean pushUp(String pid);

    List<Product> findHot();

    List<Product> findAll();

    List<Product> findByPush(int state);

    List<Product> findByPage(int page, int count);

    List<Product> findByCidPage(String pid, int page, int cout);

    Product findByPid(String pid);
}
