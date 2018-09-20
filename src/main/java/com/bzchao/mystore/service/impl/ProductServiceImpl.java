package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.ProductDao;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.ProductService;
import com.bzchao.mystore.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    @Override
    public List<Product> findHot() {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        List<Product> productList = productDao.findHot();
        sqlSession.close();
        return productList;
    }

    @Override
    public List<Product> findPage(int start, int count) {
        return null;
    }

    @Test
    public void testFindHot() {
        List<Product> list = new ProductServiceImpl().findHot();
        System.out.println(list);
    }
}
