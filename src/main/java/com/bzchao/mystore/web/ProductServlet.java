package com.bzchao.mystore.web;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.PageBean;
import com.bzchao.mystore.entity.Product;
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

    public String findCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int page = 0;
        int count = 0;
        try {
            page = Integer.valueOf(request.getParameter("page"));
            count = Integer.valueOf(request.getParameter("count"));
        } catch (NumberFormatException e) {
            System.out.println("获取页码信息错误");
        }
        page = page > 0 ? page : 1;
        count = count > 0 ? count : 6;

        String cid = request.getParameter("cid");

        List<Product> productList = new ProductServiceImpl().findByCidPage(cid, page, count);
        int total = new ProductServiceImpl().getCountByCid(cid);
        PageBean pageBean = new PageBean();
        pageBean.setList(productList);
        pageBean.setTotalCount(total);
        pageBean.setCurrPage(page);
        pageBean.setPageSize(count);

        System.out.println(pageBean);

        request.setAttribute("pageBean", pageBean);

        return "product_list";
    }
}
