<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>
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

        .span_font {
            color: #3164af;
            font-size: 18px;
            font-weight: normal;
            padding: 0 10px;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="container" style="width:100%;height:460px;background:#FF2C4C url('images/loginbg.jpg') no-repeat;">
    <div class="row">
        <div class="col-md-7">
            <!--<img src="./image/login.jpg" width="500" height="330" alt="会员登录" title="会员登录">-->
        </div>

        <div class="col-md-5">
            <div style="width:440px;border:1px solid #E7E7E7;padding:20px 0 20px 30px;border-radius:5px;margin-top:60px;background:#fff;">
                <span class="span_font">会员登录</span>USER LOGIN
                <span class="label label-danger" style="font-size: 120%;margin-left: 20px;">${message}</span>
                <div>&nbsp;</div>
                <form class="form-horizontal" action="userServlet.action">
                    <input type="hidden" name="method" value="login">
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">用户名</label>
                        <div class="col-sm-6">
                            <input type="text" name="username" class="form-control" id="username" placeholder="请输入用户名">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-6">
                            <input type="password" name="password" class="form-control" id="password"
                                   placeholder="请输入密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="code" class="col-sm-2 control-label">验证码</label>
                        <div class="col-sm-3">
                            <input type="text" name="code" class="form-control" id="code" placeholder="请输入验证码">
                        </div>
                        <div class="col-sm-3">
                            <img id="checkCode" src="checkCode.action"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="autoLogin"> 自动登录
                                </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label>
                                    <input type="checkbox"> 记住用户名
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="submit" width="100" value="登录" name="submit" border="0"
                                   style="background: url('./images/login.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
    height:35px;width:100px;color:white;">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script>
    $(function () {
        $("#checkCode").click();
    });
    $("#checkCode").click(function () {
        var codeUrl = "checkCode.action?" + Math.random();
        $(this).attr("src", codeUrl);
    });
</script>
</body>
</html>