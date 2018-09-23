package com.bzchao.mystore.web.admin;

import com.bzchao.mystore.entity.Category;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.impl.CategoryServiceImpl;
import com.bzchao.mystore.service.impl.ProductServiceImpl;
import com.bzchao.mystore.web.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.UUID;

@MultipartConfig
@WebServlet("/admin/productServlet.action")
public class AdminProductServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductServlet.class);
    public static final String UPLOAD_DIR = "upload/product";

    @Override
    public void init() throws ServletException {
        String filePath = getServletContext().getRealPath("/") + UPLOAD_DIR;
        logger.debug("初始化：{}", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String productList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> productList = new ProductServiceImpl().findByPush(0);
        req.setAttribute("productList", productList);
        return "admin/product/list";
    }

    public String productListPushDown(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> productList = new ProductServiceImpl().findByPush(1);
        req.setAttribute("productList", productList);
        return "admin/product/pushDown_list";
    }

    public String addUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categoryList = new CategoryServiceImpl().findAll();
        req.setAttribute("categoryList", categoryList);
        return "admin/product/add";
    }

    public String addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = new Product();
        try {
            BeanUtils.populate(product, req.getParameterMap());
            //对上传的文件进行处理
            String pimage = handleFile(req, resp);
            if (pimage != null) {
                product.setPimage(pimage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = "新增商品失败，数据提交不合法。";
            req.setAttribute("message", message);
            return "admin/welcome";
        }

        boolean is = new ProductServiceImpl().insert(product);
        String message = is ? "新增商品成功！" : "新增商品失败！";
        req.setAttribute("message", message);
        return "admin/welcome";
    }

    public String editUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        Product product = new ProductServiceImpl().findByPid(pid);
        List<Category> categoryList = new CategoryServiceImpl().findAll();
        req.setAttribute("product", product);
        req.setAttribute("categoryList", categoryList);
        return "admin/product/edit";
    }

    public String updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        Product product = new ProductServiceImpl().findByPid(pid);
        try {
            BeanUtils.populate(product, req.getParameterMap());
            //对上传的文件进行处理
            String pimage = handleFile(req, resp);
            if (pimage != null) {
                product.setPimage(pimage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = "改修商品失败，数据提交不合法。";
            req.setAttribute("message", message);
            return "admin/welcome";
        }

        boolean is = new ProductServiceImpl().update(product);
        String message = is ? "改修商品成功！" : "改修商品失败！";
        req.setAttribute("message", message);
        return "admin/welcome";
    }

    public String pushDown(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        boolean is = new ProductServiceImpl().pushDown(pid);
        String message = is ? "下架商品成功！" : "下架商品失败";
        req.setAttribute("message", message);
        return "admin/welcome";
    }

    public String pushUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        boolean is = new ProductServiceImpl().pushUp(pid);
        String message = is ? "上架架商品成功！" : "上架商品失败";
        req.setAttribute("message", message);
        return "admin/welcome";
    }

    public String deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        boolean is = new ProductServiceImpl().delete(pid);
        String message = is ? "删除商品成功！" : "删除商品失败";
        req.setAttribute("message", message);
        return "admin/welcome";
    }

    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*管理员登录验证*/
        String adminName = request.getParameter("username");
        String password = request.getParameter("password");
        boolean b = "admin".equals(adminName) && "123456".equals(password);
        if (!b) {
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
        }
        Cookie cookie = new Cookie("admin", adminName);
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return "home";
    }

    public String handleFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part part = request.getPart("upload");
        String fileName = part.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        fileName = UUID.randomUUID().toString().substring(0, 8) + "-" + fileName;

        String filePath = request.getServletContext().getRealPath("/") + UPLOAD_DIR + "/" + fileName;

        InputStream inputStream = part.getInputStream();
        OutputStream outputStream = new FileOutputStream(filePath);
        IOUtils.copy(inputStream, outputStream);

        return UPLOAD_DIR + "/" + fileName;
    }
}
