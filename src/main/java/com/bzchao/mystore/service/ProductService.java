package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findHot();

    List<Product> findPage(int start, int count);
}
