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
    public int getCountAll() {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        int count = productDao.getCountAll();
        sqlSession.close();
        return count;
    }

    @Override
    public int getCountByCid(String cid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        int count = productDao.getCountByCid(cid);
        sqlSession.close();
        return count;
    }

    @Override
    public List<Product> findHot() {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        List<Product> productList = productDao.findHot();
        sqlSession.close();
        return productList;
    }

    @Override
    public List<Product> findByCidPage(String cid, int currentPage, int pageSize) {

        currentPage = currentPage > 0 ? currentPage : 1;
        pageSize = pageSize > 0 ? pageSize : 6;
        int start = (currentPage - 1) * pageSize;

        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        List<Product> productList = productDao.findByCidLimit(cid, start, pageSize);
        sqlSession.close();
        return productList;
    }

    @Test
    public void testFindHot() {
        List<Product> list = new ProductServiceImpl().findByCidPage("1", 1, 6);
        System.out.println(list);
    }
}
