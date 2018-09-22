package com.bzchao.mystore.service.impl;

import com.bzchao.mystore.dao.CategoryDao;
import com.bzchao.mystore.entity.Category;
import com.bzchao.mystore.service.CategoryService;
import com.bzchao.mystore.utils.CacheUtils;
import com.bzchao.mystore.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Category> findAll() {
        List<Category> categoryList = (List<Category>) CacheUtils.get("category");

        if (categoryList == null) {
            System.out.println("CategoryServiceImpl 缓存中不存在数据，从数据库获取分类...");
            SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
            CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);
            categoryList = categoryDao.findAll();
            CacheUtils.put("category", categoryList);
            sqlSession.close();
        }

        return categoryList;
    }

    @Override
    public Category findByCid(String cid) {
        SqlSession sqlSession = MybatisUtil.getSessionFactory().openSession();
        CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);
        Category category = categoryDao.findByCid(cid);
        sqlSession.close();
        return category;
    }
    
}
