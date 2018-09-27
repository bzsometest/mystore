package com.bzchao.mystore.web.product;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.Category;
import com.bzchao.mystore.service.impl.CategoryServiceImpl;
import com.bzchao.mystore.web.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/categoryServlet.action")
public class CategoryServlet extends BaseServlet {
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Category> categoryList = new CategoryServiceImpl().findAll();
        String jsonString = JSON.toJSONString(categoryList);
        response.getWriter().write(jsonString);
        return null;
    }
}
