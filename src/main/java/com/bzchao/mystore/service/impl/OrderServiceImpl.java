package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.OrderDao;
import com.bzchao.mystore.dao.OrderItemDao;
import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;
import com.bzchao.mystore.service.OrderService;
import com.bzchao.mystore.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Override
    public Order findByOidWithItem(String oid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        Order order = orderDao.findByOidWithItem(oid);
        sqlSession.close();
        return order;
    }

    @Override
    public boolean insert(Order order) {

        String oid = UUID.randomUUID().toString().replaceAll("-", "");

        // 初始化订单信息
        order.setOid(oid);
        order.setState(0);

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
        Order byOid = findByOidWithItem("1234");
        System.out.println(byOid);
    }
}
