package com.bzchao.mystore.service;

import com.bzchao.mystore.entity.User;

public interface UserService {

    User findByUsername(String username);

    boolean register(User user);

    User login(User user);

    boolean active(String code);

    void setWebPath(String webPath);
}
