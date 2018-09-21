package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findByCid(String cid);
}
