package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.OrderDao;
import com.bzchao.mystore.dao.OrderItemDao;
import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;
import com.bzchao.mystore.entity.Product;
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

        for (Order order : orderList) {
            //对商品总价进行处理
            handleTotalPrice(order);
        }

        sqlSession.close();
        return orderList;
    }

    @Override
    public Order findByOidWithAll(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        Order order = orderDao.findByOidWithAll(oid);

        //对商品总价进行处理
        handleTotalPrice(order);

        sqlSession.close();
        return order;
    }

    @Override
    public Order insert(Order order) {

        String oid = UUID.randomUUID().toString().replaceAll("-", "");

        // 初始化订单信息
        order.setOid(oid);
        order.setState(0);
        order.setOrderTime(new Date());

        //对商品总价进行处理
        handleTotalPrice(order);

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        //在数据库中创建订单
        int res = orderDao.insert(order);

        //在数据库中创建商品条目列表
        addOrderItem(order);

        sqlSession.commit();
        sqlSession.close();
        if (res > 0) {
            return order;
        }
        return null;
    }

    @Override
    public boolean update(Order order) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        int res = orderDao.update(order);
        sqlSession.commit();
        sqlSession.close();
        return res > 0;
    }

    @Override
    public boolean insertOrderItem(OrderItem orderItem) {
        String item_id = UUID.randomUUID().toString().replace("-", "");
        orderItem.setItemId(item_id);

        //对商品小计进行处理
        handleSubPrice(orderItem);

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        int res = orderItemDao.insert(orderItem);

        sqlSession.commit();
        sqlSession.close();
        return res > 0;
    }

    @Override
    public boolean delete(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        int res = orderDao.delete(oid);
        sqlSession.commit();
        sqlSession.close();
        return res > 0;
    }

    /**
     * 在数据库中创建商品条目列表
     */
    public void addOrderItem(Order order) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);

        for (OrderItem orderItem : order.getOrderItemList()) {
            String itemId = UUID.randomUUID().toString().replaceAll("-", "");
            orderItem.setItemId(itemId);
            orderItem.setOid(order.getOid());

            //对商品小计进行处理
            handleSubPrice(orderItem);

            orderItemDao.insert(orderItem);
        }
    }

    /**
     * 处理商品条目小计
     *
     * @param orderItem
     */
    public void handleSubPrice(OrderItem orderItem) {
        double subPrice = orderItem.getProduct().getShopPrice() * orderItem.getCount();
        orderItem.setSubPrice(subPrice);
    }

    /**
     * 处理订单总价
     *
     * @param order
     */
    public void handleTotalPrice(Order order) {
        double totalPrice = 0.0;
        for (OrderItem orderItem : order.getOrderItemList()) {
            //对商品小计进行处理
            handleSubPrice(orderItem);

            totalPrice += orderItem.getSubPrice();
        }
        System.out.println(order.getOid() + ":  " + totalPrice);
        order.setTotalPrice(totalPrice);
    }

    @Test
    public void test() {
        List<Order> order = findByUidWithAll("f55b7d3a352a4f0782c910b2c70f1ea4");
        System.out.println(order);
    }
}
