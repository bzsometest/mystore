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
<jsp:include page="header.jsp"></jsp:include>

<div class="container-fluid">
    <div class="main_con">
        ${message}
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

</body>

</html>