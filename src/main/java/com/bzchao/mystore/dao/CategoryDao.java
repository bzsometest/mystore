package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();

    Category findByCid(String cid);
}
