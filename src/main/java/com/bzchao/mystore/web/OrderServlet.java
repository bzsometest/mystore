package com.bzchao.mystore.web;

import com.bzchao.mystore.entity.CartItem;
import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.entity.OrderItem;
import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.impl.OrderServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/orderServlet.action")
public class OrderServlet extends BaseServlet {

    private static final String CART_NAME = "cartItemMap";

    public String confirmOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取前端用户提交的订单信息
        Order order = new Order();
        try {
            BeanUtils.populate(order, req.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "订单数据异常！");
            return "info";
        }

        // 设置订单用户uid
        User user = (User) req.getSession().getAttribute("user");
        order.setUid(user.getUid());

        // 从购物车中读取商品条目列表
        ArrayList<OrderItem> orderItemList = getOrderItem(req, resp);
        order.setOrderItemList(orderItemList);

        // 创建订单
        boolean is = new OrderServiceImpl().insert(order);

        // 对创建订单结果进行判断处理
        if (is) {
            return REDIRECT + "order_list.jsp";
        } else {
            req.setAttribute("message", "创建订单失败！");
            return "info";
        }
    }

    /**
     * 从购物车中读取商品条目列表
     */
    private ArrayList<OrderItem> getOrderItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //从购物车中读取商品条目
        HttpSession session = req.getSession();
        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);

        //生成商品条目列表
        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        for (Map.Entry<String, CartItem> entry : cartItemMap.entrySet()) {
            CartItem cartItem = entry.getValue();
            OrderItem orderItem = new OrderItem();
            orderItem.setCount(cartItem.getCount());
            orderItem.setPid(cartItem.getProduct().getPid());
            orderItem.setSubPrice(cartItem.getSubPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }
}



