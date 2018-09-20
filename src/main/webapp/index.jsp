<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>WEB01</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
</head>

<body>
<div class="container-fluid">

    <jsp:include page="header.jsp"></jsp:include>

    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：轮播条
    -->
    <div class="container-fluid">
        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
            <!-- Indicators -->
            <ol class="carousel-indicators">
                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
            </ol>

            <!-- Wrapper for slides -->
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img src="img/1.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
                <div class="item">
                    <img src="img/2.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
                <div class="item">
                    <img src="img/3.jpg">
                    <div class="carousel-caption">

                    </div>
                </div>
            </div>

            <!-- Controls -->
            <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>
    <!--
        作者：ci2713@163.com
        时间：2015-12-30
        描述：商品显示
    -->
    <div class="container-fluid">
        <div class="col-md-12">
            <h2>热门商品&nbsp;&nbsp;<img src="img/title2.jpg"/></h2>
        </div>
        <div class="col-md-2" style="border:1px solid #E7E7E7;border-right:0;padding:0;">
            <img src="products/hao/big01.jpg" width="205" height="404" style="display: inline-block;"/>
        </div>
        <div class="col-md-10">
            <div class="col-md-6" style="text-align:center;height:200px;padding:0px;">
                <a href="product_info.htm">
                    <img src="products/hao/middle01.jpg" width="516px" height="200px" style="display: inline-block;">
                </a>
            </div>

            <div class="product_hot_item col-md-2" style="text-align:center;height:200px;padding:10px 0px;">
                <a class="product_image" href="product_info.htm">
                    <img src="products/hao/small03.jpg" width="130" height="130" style="display: inline-block;">
                </a>
                <p><a class="product_name" href="product_info.html" style='color:#666'>正在加载商品列表</a></p>
                <p><span class="product_price" color="#E4393C" style="font-size:16px">&yen;299.00</span></p>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>
</div>


<script>
    $(function () {
        var product_hot_content = $(".product_hot_item").parent();
        var product_hot_item_old = $(".product_hot_item");
        var product_hot_item = product_hot_item_old.clone();
        $.getJSON("productServlet.action", {method: "findHot"}, function (data) {
            product_hot_item_old.remove();
            $.each(data, function (i, product) {
                product_hot_item = product_hot_item.clone();
                product_hot_item.find(".product_image img").attr("src", product.pimage);
                product_hot_item.find(".product_name").text(product.pname);
                product_hot_item.find(".product_price").text(product.marketPrice);
                product_hot_content.append(product_hot_item);
            });
        });
    });
</script>
</body>

</html>