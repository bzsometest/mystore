package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> findHot();

    List<Product> findLimit(int start, int count);
}
