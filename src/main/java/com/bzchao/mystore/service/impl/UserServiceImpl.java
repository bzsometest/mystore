package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.UserDao;
import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.UserService;
import com.bzchao.mystore.utils.MailUtils;
import com.bzchao.mystore.utils.MybatisUtil;
import com.bzchao.mystore.utils.ServletUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

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
        //两次生成的UUID不能相同
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        user.setUid(uid);

        //设置激活码
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        user.setCode(code);
        //设置状态
        user.setState(0);

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        int res = userDao.save(user);
        sqlSession.commit();
        sqlSession.close();

        if (res < 1) {
            return false;
        }

        sendRegisterEmail(user);

        return true;
    }

    @Override
    public User login(User user) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User userLogin = userDao.login(user);

        sqlSession.close();
        return userLogin;
    }

    @Override
    public boolean active(String code) {

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);
        int count = userDao.updateState(code);
        sqlSession.commit();
        sqlSession.close();

        return count > 0;
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
