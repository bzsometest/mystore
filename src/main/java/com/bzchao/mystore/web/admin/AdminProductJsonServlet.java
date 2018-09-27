package com.bzchao.mystore.web.admin;

import com.alibaba.fastjson.JSON;
import com.bzchao.mystore.entity.Category;
import com.bzchao.mystore.entity.PageBean;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.impl.CategoryServiceImpl;
import com.bzchao.mystore.service.impl.ProductServiceImpl;
import com.bzchao.mystore.web.BaseJsonServlet;
import com.bzchao.mystore.web.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MultipartConfig
@WebServlet("/admin/productJsonServlet.action")
public class AdminProductJsonServlet extends BaseJsonServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductJsonServlet.class);
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

    public Object productAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> productList = new ProductServiceImpl().findByPush(0);
        return productList;
    }

    public Object productByPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int count = 10;
        try {
            page = Integer.valueOf(req.getParameter("page"));
            count = Integer.valueOf(req.getParameter("count"));
        } catch (NumberFormatException e) {
            System.out.println("获取页码信息错误,使用默认参数");
        }
        ProductServiceImpl productService = new ProductServiceImpl();
        List<Product> productList = productService.findByPage(page, count);
        int totalCount = productService.getCountAll();

        PageBean pageBean = new PageBean();
        pageBean.setList(productList);
        pageBean.setCurrPage(page);
        pageBean.setPageSize(count);
        pageBean.setTotalCount(totalCount);

        return pageBean;
    }

    public Object categoryAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categoryAll = new CategoryServiceImpl().findAll();
        return categoryAll;
    }

    public Object productListPushDown(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> productList = new ProductServiceImpl().findByPush(1);
        return productList;
    }

    public Object addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = new Product();
        Map<String, Object> map = new HashMap<>();
        try {
            StringBuilder jsonString = new StringBuilder();
            BufferedReader reader = req.getReader();
            String string = "";
            while ((string = reader.readLine()) != null) {
                jsonString.append(string);
            }
            product = JSON.parseObject(jsonString.toString(), Product.class);
        } catch (Exception e) {
            e.printStackTrace();

            String message = "新增商品失败，数据提交不合法。";
            map.put("code", "0");
            map.put("message", message);
            return map;
        }

        boolean is = new ProductServiceImpl().insert(product);
        String message = is ? "新增商品成功！" : "新增商品失败！";
        map.put("code", is ? 1 : 0);
        map.put("message", message);
        return map;
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
            return message;
        }

        boolean is = new ProductServiceImpl().update(product);
        String message = is ? "改修商品成功！" : "改修商品失败！";
        return message;
    }

    public Object uploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pimage = handleFile(request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("pimage", pimage);
        return map;
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
