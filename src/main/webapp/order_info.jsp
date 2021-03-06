﻿<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <base href="${pageContext.request.contextPath}/">
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单信息</title>
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

        <div style="margin:0 auto;margin-top:10px;width:950px;">
            <strong>订单详情</strong>
            <table class="table table-bordered">
                <tbody>
                <tr class="warning">
                    <th colspan="5">订单编号:${order.oid}</th>
                </tr>
                <tr class="warning">
                    <th>图片</th>
                    <th>商品</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>小计</th>
                </tr>
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
                </tbody>
            </table>
        </div>

        <div style="text-align:right;margin-right:120px;">
            商品金额: <strong style="color:#ff6600;">￥${order.totalPrice}元</strong>
        </div>

    </div>

    <div class="container-fluid">
        <hr/>
        <%--订单信息表单--%>
        <form id="orderForm" action="orderServlet.action" class="form-horizontal"
              style="margin:5px;margin-left: 100px;">
            <input type="hidden" name="oid" value="${order.oid}">
            <input type="hidden" name="method" value="updateOrder">
            <div class="form-group">
                <label for="address" class="col-sm-1 control-label">地址</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="address" id="address" placeholder="请输入收货地址"
                           value="${order.address}">
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-1 control-label">收货人</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="name" id="name" placeholder="请输收货人"
                           value="${order.name}">
                </div>
            </div>
            <div class="form-group">
                <label for="telephone" class="col-sm-1 control-label">电话</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="telephone" id="telephone"
                           placeholder="请输入联系方式" value="${order.telephone}">
                </div>
            </div>

        </form>

        <hr/>
        <%--订单支付表单--%>
        <form id="playForm" action="playServlet.action" class="form-horizontal">
            <div class="container" style="margin-top:5px;margin-left: 100px;">
                <strong>选择银行：</strong>
                <p>
                    <input type="hidden" name="oid" value="${order.oid}">
                    <input type="hidden" name="method" value="playOrder">
                    <br/>
                    <input type="radio" name="pd_FrpId" value="CCB-NET-B2C" checked="checked"/>建设银行
                    <img src="./bank_img/ccb.bmp" align="middle"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="pd_FrpId" value="BOC-NET-B2C"/>中国银行
                    <img src="./bank_img/bc.bmp" align="middle"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="pd_FrpId" value="ABC-NET-B2C"/>农业银行
                    <img src="./bank_img/abc.bmp" align="middle"/>
                    <br/>
                    <br/>
                    <input type="radio" name="pd_FrpId" value="CMBCHINA-NET-B2C"/>招商银行
                    <img src="./bank_img/cmb.bmp" align="middle"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="pd_FrpId" value="PINGANBANK-NET"/>平安银行
                    <img src="./bank_img/pingan.bmp" align="middle"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="pd_FrpId" value="CEB-NET-B2C"/>光大银行
                    <img src="./bank_img/guangda.bmp" align="middle"/>
                    <br/>
                    <br/>
                    <input type="radio" name="pd_FrpId" value="ICBC-NET-B2C"/>工商银行
                    <img src="./bank_img/icbc.bmp" align="middle"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="pd_FrpId" value="BOCO-NET-B2C"/>交通银行
                    <img src="./bank_img/bcc.bmp" align="middle"/>
                </p>
            </div>
        </form>

        <hr/>
        <p style="text-align:right;margin-right:200px;">
            <a href="javascript:document.getElementById('orderForm').submit();">
                <button class="btn btn-info btn-lg">更新订单信息</button>
            </a>
            <a href="javascript:document.getElementById('playForm').submit();" target="_blank">
                <button class="btn btn-warning btn-lg">立即支付订单</button>
            </a>
        </p>
        <hr/>
    </div>

</div>

<jsp:include page="footer.jsp"></jsp:include>

</body>

</html>