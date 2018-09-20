package com.bzchao.mystore.web;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.Category;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.impl.CategoryServiceImpl;
import com.bzchao.mystore.service.impl.ProductServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/productServlet.action")
public class ProductServlet extends BaseServlet {
    public String findHot(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> productList = new ProductServiceImpl().findHot();
        String jsonString = JSON.toJSONString(productList);
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        return null;
    }
}
