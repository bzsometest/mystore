<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>商品列表</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="css/style.css" type="text/css"/>0
    ++
    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
            width: 100%;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }
    </style>
</head>

<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="row" style="width:1210px;margin:0 auto;">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><a href="#">首页</a></li>
        </ol>
    </div>

    <c:forEach items="${pageBean.list}" var="product">
        <div class="col-md-2">
            <a href="productServlet.action?method=findByPid&pid=${product.pid}">
                <img src="${product.pimage}" width="170" height="170" style="display: inline-block;">
            </a>
            <p><a href="productServlet.action?method=findByPid&pid=${product.pid}"
                  style='color:green'>${product.pname}</a></p>
            <p><span color="#FF0000">商城价：&yen;${product.marketPrice}</span></p>
        </div>
    </c:forEach>

</div>

<!--分页 -->
<div style="width:380px;margin:0 auto;margin-top:50px;">
    <ul class="pagination" style="text-align:center; margin-top:10px;">

        <%--上一页按钮--%>
        <c:if test="${pageBean.currPage > 1}">
            <li>
                <a href="productServlet.action?method=findCategory&cid=${param.cid}&count=6&page=${pageBean.currPage - 1}"
                   aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
            </li>
        </c:if>

        <c:if test="${pageBean.currPage <= 1}">
            <li class="disabled">
                <a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
            </li>
        </c:if>

        <%--所有页码--%>
        <c:forEach begin="1" end="${pageBean.totalPage}" var="i">
            <c:if test="${pageBean.currPage != i}">
                <li><a href="productServlet.action?method=findCategory&cid=${param.cid}&count=6&page=${i}">${i}</a></li>
            </c:if>

            <%--当前页码按钮--%>
            <c:if test="${pageBean.currPage == i}">
                <li><a href="productServlet.action?method=findCategory&cid=${param.cid}&count=6&page=${i}">${i}</a></li>
            </c:if>
        </c:forEach>

        <%--下一页按钮--%>
        <c:if test="${pageBean.currPage < pageBean.totalPage}">
            <li>
                <a href="productServlet.action?method=findCategory&cid=${param.cid}&count=6&page=${pageBean.currPage + 1}"
                   aria-label="Next"><span aria-hidden="true">&raquo;</span> </a>
            </li>
        </c:if>
        <c:if test="${pageBean.currPage >= pageBean.totalPage}">
            <li class="disabled">
                <a href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span> </a>
            </li>
        </c:if>
    </ul>
</div>
<!-- 分页结束=======================        -->

<!--
       商品浏览记录:
-->
<div style="width:1210px;margin:0 auto; padding: 0 9px;border: 1px solid #ddd;border-top: 2px solid #999;height: 246px;">

    <h4 style="width: 50%;float: left;font: bold 14px 微软雅黑;">浏览记录</h4>
    <div style="width: 50%;float: right;text-align: right;"><a href="">more</a></div>
    <div style="clear: both;"></div>

    <div style="overflow: hidden;">
        <ul style="list-style: none;">
            <li style="width: 150px;height: 216px;float: left;margin: 0 8px 0 0;padding: 0 18px 15px;text-align: center;">
                <img src="products/1/cs10001.jpg" width="130px" height="130px"/>
            </li>
        </ul>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

</body>

</html>