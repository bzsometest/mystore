package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.OrderDao;
import com.bzchao.mystore.dao.OrderItemDao;
import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;
import com.bzchao.mystore.service.OrderService;
import com.bzchao.mystore.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> findByUidWithAll(String uid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        List<Order> orderList = orderDao.findByUidWithAll(uid);
        sqlSession.close();
        return orderList;
    }

    @Override
    public Order findByOidWithAll(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        Order order = orderDao.findByOidWithAll(oid);
        sqlSession.close();
        return order;
    }

    @Override
    public boolean insert(Order order) {

        String oid = UUID.randomUUID().toString().replaceAll("-", "");

        // 初始化订单信息
        order.setOid(oid);
        order.setState(0);
        order.setOrderTime(new Date());

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        //创建订单
        int res = orderDao.insert(order);

        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        //创建商品条目列表
        for (OrderItem orderItem : order.getOrderItemList()) {
            String itemId = UUID.randomUUID().toString().replaceAll("-", "");
            orderItem.setItemId(itemId);
            orderItem.setOid(order.getOid());
            orderItemDao.insert(orderItem);
        }

        sqlSession.commit();
        sqlSession.close();

        return res > 0;
    }

    @Override
    public boolean insertOrderItem(OrderItem orderItem) {
        String item_id = UUID.randomUUID().toString().replace("-", "");
        orderItem.setItemId(item_id);

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        int res = orderItemDao.insert(orderItem);
        sqlSession.commit();
        sqlSession.close();
        return res > 0;
    }

    @Test
    public void test() {
        List<Order> byOid = findByUidWithAll("f55b7d3a352a4f0782c910b2c70f1ea4");
        System.out.println(byOid);
    }

    @Test
    public void testFindOrder() {
        Order item = findByOidWithAll("1234");

        System.out.println(item);
    }

    @Test
    public void testItem() {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        OrderItem order = orderItemDao.findByItemIdWithProduct("f4464e8934e449e9840111cc3385fa38");
        System.out.println(order);

    }
}
