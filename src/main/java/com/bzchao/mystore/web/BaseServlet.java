package com.bzchao.mystore.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    public static final String REDIRECT = "redirect:";

    private final static Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    /**
     * 通过method参数调用(反射)不同的处理方法
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("请求数据：{},参数：{}", req.getRequestURI(), req.getQueryString());
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain;charset=UTF-8");

        //1. 获取method参数
        String methodName = req.getParameter("method");

        //2. 通过反射调用方法
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            String view = (String) method.invoke(this, req, resp);

            if (view != null) {
                //是否有redirect:字符串
                if (view.contains(REDIRECT)) {
                    //redirect:login.jsp
                    //重定向
                    resp.sendRedirect(view.replaceAll(REDIRECT, ""));
                    return;
                } else {
                    //请求转发
                    req.getRequestDispatcher("/" + view + ".jsp").forward(req, resp);
                    return;
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            req.setAttribute("message", this.getClass().getName() + "中没有找到方法:" + methodName);
            req.getRequestDispatcher("/info.jsp").forward(req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("http://www.bzchao.com/404.htm");
        }
    }
}
