<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的购物车</title>
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

        .container .row div {
            /* position:relative;
 float:left; */
        }

        font {
            color: #3164af;
            font-size: 18px;
            font-weight: normal;
            padding: 0 10px;
        }
    </style>
</head>

<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="container">
    <div class="row">

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong style="font-size:16px;margin:5px 0;">订单详情</strong>
            <table class="table table-bordered">
                <thead>
                <tr class="warning">
                    <th>图片</th>
                    <th>商品</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>小计</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cart.cartItemMap}" var="cartItem">
                    <tr class="active">
                        <td width="60" width="40%">
                            <input type="hidden" name="pid" value="${cartItem.key}">
                            <img src="${cartItem.value.product.pimage}" width="70" height="60">
                        </td>
                        <td width="30%">
                            <a target="_blank">${cartItem.value.product.pname}</a>
                        </td>
                        <td width="20%">
                            ￥${cartItem.value.product.shopPrice}
                        </td>
                        <td width="10%">
                            <input type="text" name="quantity" value="${cartItem.value.count}" maxlength="4" size="10">
                        </td>
                        <td width="15%">
                            ￥ <span class="subPrice">${cartItem.value.subPrice}</span>
                        </td>
                        <td>
                            <a href="cartServlet.action?method=deleteCart&pid=${cartItem.key}"
                               class="delete">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div style="margin-right:130px;">
        <div style="text-align:right;">
            <em style="color:#ff6600;">
                登录后确认是否享有优惠&nbsp;&nbsp;
            </em> 赠送积分: <em style="color:#ff6600;" id="nodes">${cart.totalPrice/10}</em>&nbsp;
            商品金额: <strong style="color:#ff6600;">￥<span id="totalPrice">${cart.totalPrice}</span>元</strong>
        </div>
        <div style="text-align:right;margin-top:10px;margin-bottom:10px;">
            <a href="cartServlet.action?method=clearCart" id="clear" class="clear">清空购物车</a>
            <a href="order_info.jsp">
                <input type="submit" width="100" value="提交订单" name="submit" border="0" style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
						height:35px;width:100px;color:white;">
            </a>
        </div>
    </div>

</div>

<jsp:include page="footer.jsp"></jsp:include>

<script>
    $("input[name=quantity]").blur(function () {
        var _this_tr = $(this).parent().parent();
        var quantity = $(this).val();
        var pid = _this_tr.find("input[name=pid]").val();
        $.ajax({
            type: "POST",
            url: "cartServlet.action",
            /*指定以表单方式提交*/
            contentType: "application/x-www-form-urlencoded",
            data: {method: "updateCart", pid: pid, quantity: quantity},
            dataType: "json",
            success: function (data) {
                var totalPrice = data.totalPrice;
                //返回的数据是map键值对，直接获取
                var subPrice = data.cartItemMap[pid].subPrice;

                _this_tr.find(".subPrice").text(subPrice);
                $("#totalPrice").text(totalPrice);
                $("#nodes").text(totalPrice / 10);
            }
        });
    });
</script>
</body>

</html>