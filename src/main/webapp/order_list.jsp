<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的订单</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="css/style.css" type="text/css"/>

    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }
    </style>
</head>

<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="container">
    <div class="row">

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong>我的订单</strong>
            <c:forEach items="${orderList}" var="order">
                <table class="table table-bordered">
                    <thead>
                    <tr class="success">
                        <th colspan="3">订单编号:
                            <a href="orderServlet.action?method=showOrderByOid&oid=${order.oid}">${order.oid}</a>
                        </th>
                        <th colspan="1" style="padding: 10px">
                            <c:if test="${order.state==1}">
                                <span class="label label-success">已支付</span>
                            </c:if>
                            <c:if test="${order.state!=1}">
                                <span class="label label-warning">待支付</span>
                            </c:if>
                        </th>
                        <th colspan="1">
                            <a href="playServlet.action?method=playOrder&pd_FrpId=CCB-NET-B2C&oid=${order.oid}"
                               title="建设银行支付">支付</a>
                            <a href="orderServlet.action?method=showOrderByOid&oid=${order.oid}">修改</a>
                            <a href="orderServlet.action?method=deleteOrder&oid=${order.oid}">删除</a>
                        </th>
                    </tr>
                    <tr class="warning">
                        <th>图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${order.orderItemList}" var="orderItem">
                        <tr class="active">
                            <td width="60" width="40%">
                                <input type="hidden" name="id" value="22">
                                <img src="${orderItem.product.pimage}" width="70" height="60">
                            </td>
                            <td width="30%">
                                <a target="_blank">${orderItem.product.pname}</a>
                            </td>
                            <td width="20%">
                                ￥${orderItem.product.shopPrice}
                            </td>
                            <td width="10%">
                                    ${orderItem.count}
                            </td>
                            <td width="15%">
                                <span class="subtotal">￥${orderItem.subPrice}</span>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr class="active">
                        <td colspan="4">
                            <span style="margin-left:20px;font-size: 120%;">总计</span>
                        </td>
                        <td colspan="1">
                            <span>￥${order.totalPrice}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </c:forEach>
        </div>
    </div>
    <div style="text-align: center;">
        <ul class="pagination">
            <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
            <li class="active"><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            <li><a href="#">6</a></li>
            <li><a href="#">7</a></li>
            <li><a href="#">8</a></li>
            <li><a href="#">9</a></li>
            <li>
                <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

</body>

</html>