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
    public Order findByOidSimple(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        Order order = orderDao.findByOidSimple(oid);

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
        System.out.println("OrderServiceImpl:" + order);
        //在数据库中创建订单
        int res = orderDao.insert(order);
        sqlSession.commit();
        sqlSession.close();

        if (res < 1) {
            System.out.println("创建订单失败！");
            return null;
        }

        //警告！必须先关闭创建order的session，某则，将导致order被锁定！无法增加商品条目，导致死锁
        //在数据库中创建商品条目列表
        addOrderItem(order.getOrderItemList(), oid);

        return order;
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
    public boolean delete(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        int res = orderDao.delete(oid);
        sqlSession.commit();
        sqlSession.close();
        return res > 0;
    }


    @Override
    public void addOrderItem(List<OrderItem> orderItemList, String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);

        for (OrderItem orderItem : orderItemList) {
            String itemId = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println("addOrderItem UUID:" + itemId);
            orderItem.setItemId(itemId);
            orderItem.setOid(oid);
            //对商品小计进行处理
            handleSubPrice(orderItem);

            orderItemDao.insert(orderItem);
        }

        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 处理商品条目小计
     *
     * @param orderItem
     */
    public void handleSubPrice(OrderItem orderItem) {
        //未获得product
        Product product = orderItem.getProduct();
        double subPrice = product.getShopPrice() * orderItem.getCount();
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

            //首先读取商品信息到商品项目中，以便计算
            Product product = new ProductServiceImpl().findByPid(orderItem.getPid());
            orderItem.setProduct(product);

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
