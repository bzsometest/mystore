<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!--
时间：2015-12-30
描述：菜单栏
-->
<div class="container-fluid">
    <div class="col-md-4">
        <img src="img/logo2.png"/>
    </div>
    <div class="col-md-5">
        <img src="img/header.png"/>
    </div>
    <div class="col-md-3" style="padding-top:20px">
        <c:if test="${sessionScope.user!=null}">
            <ol class="list-inline">
                <li>用户： <a href="index.jsp" style="font-size: 150%;">${sessionScope.user.username}</a></li>
                <li><a href="userServlet.action?method=logout">注销登录</a></li>
                <li><a href="orderServlet.action?method=showOrderByUser">我的订单</a></li>
                <li><a href="cart.jsp">购物车</a></li>
            </ol>
        </c:if>
        <c:if test="${sessionScope.user==null}">
            <ol class="list-inline">
                <li><a href="login.jsp">登录</a></li>
                <li><a href="register.jsp">注册</a></li>
            </ol>
        </c:if>
    </div>
</div>
<!--
时间：2015-12-30
描述：导航条
-->
<div class="container-fluid">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp">首页</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">手机数码<span class="sr-only">(current)</span></a></li>
                    <li><a href="#">正在加载导航栏</a></li>
                    <li><a href="#">正在加载导航栏</a></li>
                </ul>
                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</div>
<script>
    $(function () {
        $.getJSON("categoryServlet.action", {method: "findAll"}, function (data) {
            $(".navbar-nav").empty();
            $.each(data, function (i, category) {
                var aLi = $("<li><a></a></li>");
                var url = "productServlet.action?method=findCategory&cid=" + category.cid;
                aLi.find("a").text(category.cname).attr("href", url);
                $(".navbar-nav").append(aLi);
            });
        });
    });
</script>
