<%@ page language="java" contentType="text/html; charset=UTF-8"

         import="com.baidu.ueditor.ActionEnter"

         pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    request.setCharacterEncoding("utf-8");
    response.setHeader("Content-Type", "text/html");

    String rootPath = application.getRealPath("/");
    String webPath = request.getContextPath();
    System.out.println(webPath);
    System.out.println(rootPath);
    String action = request.getParameter("action");

    String result = new ActionEnter(request, rootPath).exec();
    System.out.println("aa:" + result);
    if (action != null &&
            (action.equals("listfile") || action.equals("listimage"))) {
        rootPath = rootPath.replace("\\", "/");
        result = result.replaceAll(rootPath, webPath + "/");
    }
    out.write(result);
    System.out.println(result);
%>
