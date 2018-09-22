package com.bzchao.mystore.dao;

import com.bzchao.mystore.entity.User;

public interface UserDao {
    User findByUsername(String username);

    int save(User user);

    int updateState(String code);

    User login(User user);
}
