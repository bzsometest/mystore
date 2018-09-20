package com.bzchao.mystore.utils;

import com.bzchao.mystore.entity.Category;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class CacheUtils {
    private static final String CACHE_NAME = "myStore";
    private static CacheManager manager;

    static {
        URL url = CookieUtils.class.getClassLoader().getResource("ehcache.xml");
        System.out.println("url:" + url.getFile());
        manager = CacheManager.create(url);
    }

    public static Object get(String key) {
        Cache cache = manager.getCache(CACHE_NAME);
        if (cache == null || cache.get(key) == null) {
            return null;
        }
        return cache.get(key).getObjectValue();
    }

    public static void put(String key, Object value) {
        Cache cache = manager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        Element element = new Element(key, value);
        cache.put(element);
    }

    @Test
    public void testGet() {
        List<Category> categoryList = (List<Category>) get("category");
        put("a", "222");
        Object o = get("a");
        System.out.println(o);
    }
}
