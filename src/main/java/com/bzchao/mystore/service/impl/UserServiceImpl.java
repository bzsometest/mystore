package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.UserDao;
import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.UserService;
import com.bzchao.mystore.utils.MailUtils;
import com.bzchao.mystore.utils.MybatisUtil;
import com.bzchao.mystore.utils.ServletUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    private static String webPath = "";

    @Override
    public User findByUsername(String username) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User user = userDao.findByUsername(username);
        sqlSession.close();
        return user;
    }

    @Override
    public boolean register(User user) {
        boolean result = false;
        user.setUid(UUID.randomUUID().toString().replaceAll("-", ""));

        //设置激活码
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        user.setCode(code);
        //设置状态
        user.setState(0);

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        userDao.save(user);

        sqlSession.commit();
        sqlSession.close();

        //TODO 发送一封激活邮件
        sendRegisterEmail(user);

        result = true;

        return result;
    }

    @Override
    public User login(User user) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        return userDao.login(user);
    }

    @Override
    public boolean active(String code) {
        boolean result = false;

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);
        int count = userDao.updateState(code);
        sqlSession.commit();
        sqlSession.close();

        if (count == 1) {
            result = true;
        }
        return result;
    }

    private void sendRegisterEmail(final User user) {
        //在线程中发送注册邮件
        Thread thread = new Thread() {
            @Override
            public void run() {
                String code = user.getCode();
                String content = "<h1>来自天虎官方商城的激活邮件:请点击下面链接激活!</h1><h3><a href='" + webPath + "/userServlet?method=activeUser&code=" + code + "'>" + webPath + "/userServlet?method=active&code=" + code + "</a></h3>";
                MailUtils.sendMail(user.getEmail(), content);
            }
        };
        thread.start();

    }

    /**
     * 设置项目完整url路径
     *
     * @param path
     */
    @Override
    public void setWebPath(String path) {
        webPath = path;
    }
}
