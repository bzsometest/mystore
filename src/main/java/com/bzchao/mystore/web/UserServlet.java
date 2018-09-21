package com.bzchao.mystore.web;

import com.bzchao.mystore.entity.User;
import com.bzchao.mystore.service.impl.UserServiceImpl;
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
            if (is) {
                return REDIRECT + "login.jsp";
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        req.setAttribute("message", "注册失败，请重试!");
        return "register";
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
            cookie.setMaxAge(60 * 60);
            resp.addCookie(cookie);
        }

        req.getSession().setAttribute("username", newUser.getName());
        return REDIRECT + "index.jsp";
    }

    public String checkUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        System.out.println("username：" + username);
        User user = new UserServiceImpl().findByUsername(username);
        String code = user == null ? "1" : "0";
        System.out.println(code);
        resp.getWriter().write(code);
        return null;
    }

    public String activeUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        boolean is = new UserServiceImpl().active(code);
        String message = is ? "激活成功！" : "激活验证邮件无效！";
        System.out.println(message);
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
