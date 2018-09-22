package com.bzchao.mystore.web;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.Cart;
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

@WebServlet("/cartServlet.action")
public class CartServlet extends BaseServlet {
    private static final String CART_NAME = "cart";

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

        String pid = req.getParameter("pid");
        int quantity = Integer.valueOf(req.getParameter("quantity"));

        Cart cart = getCart(req, resp);
        Map<String, CartItem> cartItemMap = cart.getCartItemMap();

        //购物车中没有此商品，则初始化此商品item
        if (!cartItemMap.containsKey(pid)) {
            CartItem cartItem = new CartItem();
            Product product = new ProductServiceImpl().findByPid(pid);

            //数据库中不存在此商品
            if (product == null) {
                req.setAttribute("message", "不存在此商品！pid：" + pid);
                return "info";
            }

            //初始化商品item
            cartItem.setProduct(product);
            cartItem.setCount(0);
            cartItemMap.put(pid, cartItem);
        }

        //增加购物车中商品数量
        CartItem cartItem = cartItemMap.get(pid);
        int oldCount = cartItem.getCount();
        cartItem.setCount(oldCount + quantity);
        cartItemMap.put(pid, cartItem);

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


        Cart cart = getCart(req, resp);
        Map<String, CartItem> cartItemMap = cart.getCartItemMap();

        //购物车中不存在此商品
        if (!cartItemMap.containsKey(pid)) {
            resp.getWriter().write("error");
            return null;
        }

        CartItem cartItem = cartItemMap.get(pid);
        cartItem.setCount(quantity);

        cartItemMap.put(pid, cartItem);

        String jsonString = JSON.toJSONString(cart);

        resp.getWriter().write(jsonString);
        resp.getWriter().flush();

        return null;
    }

    public String clearCart(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(CART_NAME);
        return "redirect:cart.jsp";
    }

    public String deleteCart(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();

        //获取到的内容是原地址，修改数据不需要再写入session中
        Map<String, CartItem> cartItemMap = (Map<String, CartItem>) session.getAttribute(CART_NAME);
        cartItemMap.remove(pid);

        return "redirect:cart.jsp";
    }

    public Cart getCart(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CART_NAME);

        //用户第一次使用购物车
        if (cart == null) {
            //初始化用户购物车
            cart = new Cart();
            cart.setCartItemMap(new HashMap<String, CartItem>());
            //将购物车存放到session中
            session.setAttribute(CART_NAME, cart);
        }

        return cart;
    }
}
