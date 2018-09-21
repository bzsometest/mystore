package com.bzchao.mystore.filter;

import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.impl.UserServiceImpl;
import com.bzchao.mystore.utils.CookieUtils;

import javax.mail.Session;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AutoLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("过滤器 AutoLoginFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Cookie usernameCookie = CookieUtils.getCookie("username", httpRequest.getCookies());

            if (usernameCookie == null) {
                //用户未登录，跳转到登录页面
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
                return;
            }

            String username = usernameCookie.getValue();
            user = new UserServiceImpl().findByUsername(username);
            session.setAttribute("user", user);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
