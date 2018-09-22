package com.bzchao.mystore.web;

import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.impl.UserServiceImpl;
import com.bzchao.mystore.utils.CookieUtils;
import com.bzchao.mystore.utils.MailUtils;
import com.bzchao.mystore.utils.MyDateConverter;
import com.bzchao.mystore.utils.ServletUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@WebServlet("/userServlet.action")
public class UserServlet extends BaseServlet {

    public static final String CHECK_CODE_NAME = "checkCode";

    /**
     * 用户注册
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public String register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //编程方式：正逻辑流程,使用if对错误逻辑进行处理

        if (!validateCode(req)) {
            req.setAttribute("message", "验证码错误!");
            return "register";
        }

        User user = new User();
        try {
            ConvertUtils.register(new MyDateConverter(), Date.class);
            BeanUtils.populate(user, req.getParameterMap());
            UserServiceImpl userService = new UserServiceImpl();
            userService.setWebPath(ServletUtils.getWebPath(req));
            boolean is = userService.register(user);
            if (!is) {
                req.setAttribute("message", "注册失败，请重试!");
                return "register";
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            req.setAttribute("message", "注册失败，数据提交不符合要求!");
            return "register";
        }

        req.setAttribute("message", "注册成功，请查收注册邮件!");
        return "info";
    }

    /**
     * 用户登录
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!validateCode(req)) {
            req.setAttribute("message", "验证码错误!");
            return "login";
        }

        User user = new User();
        try {
            BeanUtils.populate(user, req.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User newUser = new UserServiceImpl().login(user);
        if (newUser == null) {
            req.setAttribute("message", "用户名或密码错误！");
            return "login";
        }
        //记住密码
        if (req.getParameter("autoLogin") != null) {
            Cookie cookie = new Cookie("username", user.getUsername());
            cookie.setMaxAge(60 * 60 * 10);
            resp.addCookie(cookie);
        }

        //一定要存取获得到新用户！某则无法获得uid
        req.getSession().setAttribute("user", newUser);
        return REDIRECT + "index.jsp";
    }

    public String logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        Cookie cookie = CookieUtils.getCookie("username", req.getCookies());
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        return REDIRECT + "login.jsp";
    }

    public String checkUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        User user = new UserServiceImpl().findByUsername(username);
        String code = user == null ? "1" : "0";
        resp.getWriter().write(code);
        return null;
    }

    public String activeUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        boolean is = new UserServiceImpl().active(code);
        String message = is ? "激活成功！" : "激活验证邮件无效！";
        req.setAttribute("message", message);
        return "info";
    }

    private boolean validateCode(HttpServletRequest req) {
        String code = req.getParameter("code");
        String codeSesion = (String) req.getSession().getAttribute(CHECK_CODE_NAME);

        if (code == null || !code.equalsIgnoreCase(codeSesion)) {
            return false;
        }
        return true;
    }

}
