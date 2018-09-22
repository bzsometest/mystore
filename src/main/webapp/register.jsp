<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>用户注册</title>
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

<div class="container" style="width:100%;background:url('images/regist_bg.jpg');">
    <div class="row">

        <div class="col-md-2"></div>

        <div class="col-md-8" style="background:#fff;padding:40px 80px;margin:30px;border:7px solid #ccc;">
            <span class="span_font">会员注册</span>USER REGISTER
            <span class="label label-danger" style="font-size: 120%;margin-left: 20px;">${message}</span>
            <form class="form-horizontal" style="margin-top:5px;" action="userServlet.action">
                <input type="hidden" name="method" value="register">
                <div class="form-group">
                    <label for="username" class="col-sm-2 control-label">用户名</label>
                    <div class="col-sm-6">
                        <input type="text" name="username" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <span id="username_result"></span>
                </div>
                <div class="form-group">
                    <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
                    <div class="col-sm-6">
                        <input type="password" name="password" class="form-control" id="inputPassword3"
                               placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group">
                    <label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="confirmpwd" placeholder="请输入确认密码">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-6"  title="请准确填写邮箱，以便成功注册。" data-placement="right">
                        <input type="email" name="email" class="form-control" id="inputEmail" placeholder="Email">
                    </div>
                </div>
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">姓名</label>
                    <div class="col-sm-6">
                        <input type="text" name="name" class="form-control" id="name" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="form-group opt">
                    <label for="inlineRadio1" class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-6">
                        <label class="radio-inline">
                            <input type="radio" name="sex" id="inlineRadio1" value="M"> 男
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="sex" id="inlineRadio2" value="F"> 女
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="date" class="col-sm-2 control-label">出生日期</label>
                    <div class="col-sm-6">
                        <input type="date" name="birthday" class="form-control" id="date">
                    </div>
                </div>

                <div class="form-group">
                    <label for="code" class="col-sm-2 control-label">验证码</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" id="code" name="code">
                    </div>
                    <div class="col-sm-2">
                        <img id="checkCode" src="checkCode.action"/>
                    </div>

                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="submit" width="100" value="注册" name="submit" border="0"
                               style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
				    height:35px;width:100px;color:white;">
                    </div>
                </div>
            </form>
        </div>

        <div class="col-md-2"></div>
    </div>
</div>


<jsp:include page="footer.jsp"></jsp:include>

<script>
    $(function () {
        $("#checkCode").click();
        $('#inputEmail').parent().tooltip('show');
    });
    $("#checkCode").click(function () {
        var codeUrl = "checkCode.action?" + Math.random();
        $(this).attr("src", codeUrl);
    });
    $("#username").blur(function () {
        validateUsername();
    });


    function validateUsername() {
        $.get("userServlet.action", {method: "checkUsername", username: $("#username").val()}, function (data) {
            if (data == 1) {
                $("#username_result").css("color", "blue").text("用户名可用！");
            } else {
                $("#username_result").css("color", "red").text("用户名不可用！");
            }
        });
    }
</script>

</body>
</html>




