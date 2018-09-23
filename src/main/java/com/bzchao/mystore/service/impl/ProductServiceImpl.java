package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.ProductDao;
import com.bzchao.mystore.entity.Product;
import com.bzchao.mystore.service.ProductService;
import com.bzchao.mystore.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

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
    public boolean insert(Product product) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        String pid = UUID.randomUUID().toString().substring(0, 16);
        product.setPid(pid);
        product.setPflag(0);

        int count = productDao.insert(product);
        sqlSession.commit();
        sqlSession.close();
        return count > 0;
    }

    public boolean update(Product product) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        int count = productDao.update(product);
        sqlSession.commit();
        sqlSession.close();
        return count > 0;
    }

    @Override
    public boolean delete(String pid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        int count = productDao.delete(pid);
        sqlSession.commit();
        sqlSession.close();
        return count > 0;
    }

    @Override
    public boolean pushDown(String pid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        int count = productDao.push(pid, 1);
        sqlSession.commit();
        sqlSession.close();
        return count > 0;
    }

    @Override
    public boolean pushUp(String pid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);

        int count = productDao.push(pid, 0);
        sqlSession.commit();
        sqlSession.close();
        return count > 0;
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
    public List<Product> findAll() {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        List<Product> productList = productDao.findAll();
        sqlSession.close();
        return productList;
    }

    @Override
    public List<Product> findByPush(int state) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        List<Product> productList = productDao.findByPush(state);
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

    @Override
    public Product findByPid(String pid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        Product product = productDao.findByPid(pid);
        sqlSession.close();
        return product;
    }

    @Test
    public void test() {
        List<Product> productList = findHot();
        System.out.println(productList);
    }


}
