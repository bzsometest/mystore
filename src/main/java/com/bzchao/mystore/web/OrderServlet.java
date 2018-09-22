package com.bzchao.mystore.web;

import com.bzchao.mystore.entity.*;
import com.bzchao.mystore.service.impl.OrderServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/orderServlet.action")
public class OrderServlet extends BaseServlet {

    private static final String CART_NAME = "cart";

    /**
     * 新增用户订单
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String addOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取前端用户提交的订单信息
        Order order = new Order();

        // 设置订单用户uid
        User user = (User) req.getSession().getAttribute("user");
        order.setUid(user.getUid());

        // 从购物车中读取商品条目列表
        ArrayList<OrderItem> orderItemList = getOrderItem(req, resp);
        order.setOrderItemList(orderItemList);

        // 在数据库中创建订单
        Order newOrder = new OrderServiceImpl().insert(order);

        // 对创建订单结果进行判断处理
        if (newOrder != null) {
            //创建订单成功
            //删除购物车中的商品
            req.getSession().removeAttribute(CART_NAME);
            //跳转到订单显示页面(Servlet)
            return REDIRECT + "orderServlet.action?method=showOrderByOid&oid=" + order.getOid();
        } else {
            req.setAttribute("message", "创建订单失败！");
            return "info";
        }
    }

    /**
     * 更新订单
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String updateOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = orderService.findByOidWithAll(oid);

        //获取前端用户提交的订单信息
        try {
            BeanUtils.populate(order, req.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "订单数据获取处理异常！");
            return "info";
        }

        boolean is = orderService.update(order);
        // 对创建订单结果进行判断处理
        if (is) {
            //更新订单信息成功
            //跳转到支付页面
            return REDIRECT + "orderServlet.action?method=showOrderByOid&oid=" + oid;
        } else {
            req.setAttribute("message", "更新订单信息失败！");
            return "info";
        }
    }

    public String showOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = orderService.findByOidWithAll(oid);
        req.setAttribute("order", order);
        return "order_info";
    }

    public String showOrderByUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Order> orderList = new OrderServiceImpl().findByUidWithAll(user.getUid());

        req.setAttribute("orderList", orderList);
        return "order_list";
    }

    public String deleteOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        boolean is = new OrderServiceImpl().delete(oid);
        if (!is) {
            req.setAttribute("message", "删除订单失败！");
        }

        return REDIRECT + "orderServlet.action?method=showOrderByUser";
    }

    /**
     * 从购物车中读取商品条目列表
     */
    private ArrayList<OrderItem> getOrderItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //从购物车中读取商品条目
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute(CART_NAME);
        Map<String, CartItem> cartItemMap = cart.getCartItemMap();

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



