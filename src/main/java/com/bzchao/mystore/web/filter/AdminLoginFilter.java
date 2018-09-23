package com.bzchao.mystore.web.filter;

import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.impl.UserServiceImpl;
import com.bzchao.mystore.utils.CookieUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String  adminName = (String) session.getAttribute("admin");
        if (adminName == null) {
            Cookie usernameCookie = CookieUtils.getCookie("admin", httpRequest.getCookies());
            if (usernameCookie == null) {
                //用户未登录，跳转到登录页面
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login.jsp");
                return;
            }
            adminName = usernameCookie.getValue();
            session.setAttribute("admin", adminName);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
