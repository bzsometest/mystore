package com.bzchao.mystore.web;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.CartItem;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet("/cartServlet.action")
public class CartServlet extends BaseServlet {
    private static final String CART_NAME = "cartItemMap";

    /**
     * 增加指定数量的商品到购物车
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String addCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String pid = req.getParameter("pid");
        int count = Integer.valueOf(req.getParameter("count"));

        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);
        //  用户第一次使用购物车
        if (cartItemMap == null) {
            cartItemMap = new HashMap<String, CartItem>();
        }
        //购物车中没有此商品，则初始化此商品item
        if (!cartItemMap.containsKey(pid)) {
            CartItem cartItem = new CartItem();
            Product product = new ProductServiceImpl().findByPid(pid);
            if (product == null) {
                //数据库中不存在此商品
                req.setAttribute("message", "不存在此商品！pid：" + pid);
                return "info";
            }
            cartItem.setProduct(product);
            cartItem.setCount(0);
            cartItemMap.put(pid, cartItem);
        }

        CartItem cartItem = cartItemMap.get(pid);
        int oldCount = cartItem.getCount();
        cartItem.setCount(oldCount + count);
        cartItemMap.put(pid, cartItem);

        session.setAttribute(CART_NAME, cartItemMap);

        double totalPrice = getTotalPrice(req, resp);
        session.setAttribute("totalPrice", totalPrice);

        //采用响应重定向，防止用户刷新页面，数据被重新提交
        return REDIRECT + "cart.jsp";
    }

    /**
     * 更新购物车中商品数量
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String updateCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String pid = req.getParameter("pid");
        int quantity = Integer.valueOf(req.getParameter("quantity"));

        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);

        CartItem cartItem = cartItemMap.get(pid);
        if (cartItem == null) {
            resp.getWriter().write("error");
            return null;
        }
        cartItem.setCount(quantity);
        cartItemMap.put(pid, cartItem);

        double totalPrice = getTotalPrice(req, resp);
        session.setAttribute("totalPrice", totalPrice);

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("cartItem", cartItem);
        respMap.put("totalPrice", totalPrice);

        String jsonString = JSON.toJSONString(respMap);

        resp.getWriter().write(jsonString);
        resp.getWriter().flush();

        return null;
    }

    public String clearCart(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(CART_NAME);
        double totalPrice = getTotalPrice(request, response);
        request.getSession().setAttribute("totalPrice", totalPrice);
        return "redirect:cart.jsp";
    }

    public String deleteCart(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();

        //获取到的内容是原地址，修改数据不需要再写入session中
        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);
        cartItemMap.remove(pid);

        double totalPrice = getTotalPrice(request, response);
        request.getSession().setAttribute("totalPrice", totalPrice);

        return "redirect:cart.jsp";
    }

    public static double getTotalPrice(HttpServletRequest request, HttpServletResponse response) {
        double totalPrice = 0.0;
        HttpSession session = request.getSession();
        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);
        if (cartItemMap == null) {
            return totalPrice;
        }
        for (Map.Entry<String, CartItem> entry : cartItemMap.entrySet()) {
            double subPrice = entry.getValue().getSubPrice();
            totalPrice += subPrice;
        }
        return totalPrice;
    }
}
