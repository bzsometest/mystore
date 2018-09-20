package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductService {

    int getCountAll();

    int getCountByCid(String cid);

    List<Product> findHot();

    List<Product> findByCidPage(String pid, int page, int cout);
}
