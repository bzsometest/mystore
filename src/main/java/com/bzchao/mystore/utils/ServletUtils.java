package com.bzchao.mystore.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {
    public static String getWebPath(HttpServletRequest httpRequest) {
        String scheme = httpRequest.getScheme();//http
        String serverName = httpRequest.getServerName();//localhost
        int serverPort = httpRequest.getServerPort();//8080
        String contextPath = httpRequest.getContextPath();//项目名
        String webPath = scheme + "://" + serverName + ":" + serverPort + contextPath;//http://127.0.0.1:8080/test
        if (serverPort == 80) {
            //端口是80，则不显示端口
            webPath = scheme + "://" + serverName + contextPath;//http://127.0.0.1:8080/test
        }
        return webPath;
    }
}
