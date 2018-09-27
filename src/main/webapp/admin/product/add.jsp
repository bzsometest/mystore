<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
<HEAD>
    <base href="${pageContext.request.contextPath}/">
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <LINK href="${pageContext.request.contextPath}/css/Style1.css" type="text/css" rel="stylesheet">
</HEAD>

<body>
<!--  -->
<form id="Form1" name="Form1" method="post">
    <table cellSpacing="1" cellPadding="5" width="100%" align="center" bgColor="#eeeeee"
           style="border: 1px solid #8ba7e3" border="0">
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3" colSpan="4"
                height="26">
                <STRONG>添加商品</STRONG>
            </td>
        </tr>

        <tr>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                商品名称：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <input type="text" name="pname" id="pname" class="bg" v-model="product.pname" style="width:280px;"/>
            </td>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                是否热门：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <select name="isHot" v-model="product.isHot">
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
            </td>
        </tr>
        <tr>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                市场价格：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <input type="text" name="marketPrice" id="marketPrice" class="bg" v-model="product.marketPrice"
                       style="width:280px;"/>
            </td>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                商城价格：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <input type="text" name="shopPrice" id="shopPrice" class="bg" v-model="product.shopPrice"
                       style="width:280px;"/>
            </td>
        </tr>
        <tr>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                商品图片：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <input type="file" name="upload" @blur="uploadImage" style="width:280px;"/>
            </td>

            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                上传文件路径：
            </td>
            <td class="ta_01" bgColor="#ffffff">
                <input type="text" name="pimage" class="bg" v-model="product.pimage" placeholder="上传文件或输入远程url"
                       style="width:280px;"/>
            </td>
        </tr>
        <tr>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                所属的分类：
            </td>
            <td class="ta_01" bgColor="#ffffff" colspan="3">
                <select name="cid" v-model="product.cid">
                    <option v-for="category in categoryList" :value="category.cid">
                        {{category.cname}}
                    </option>
                </select>
            </td>
        </tr>
        <tr>
            <td width="18%" align="center" bgColor="#f5fafe" class="ta_01">
                商品描述：
            </td>
            <td width="80%" class="ta_01" bgColor="#ffffff" colspan="3">
                <!-- 加载编辑器的容器 -->
                <script id="container" name="pdesc" type="text/plain">{{product.pdesc}} </script>
                <!-- 配置文件 -->
                <script type="text/javascript"
                        src="${pageContext.request.contextPath}/ueditor/ueditor.config.js"></script>
                <!-- 编辑器源码文件 -->
                <script type="text/javascript" src="${pageContext.request.contextPath}/ueditor/ueditor.all.js"></script>
                <!-- 实例化编辑器 -->
                <script type="text/javascript">
                    var ue = UE.getEditor('container');
                </script>
            </td>
        </tr>
        <tr>
            <td class="ta_01" style="WIDTH: 100%" align="center"
                bgColor="#f5fafe" colSpan="4">
                <%--显示指定type为button，阻止表单自动提交--%>
                <button type="button" id="userAction_save_do_submit" value="确定" class="button_ok" @click="submit">
                    &#30830;&#23450;
                </button>

                <FONT face="宋体">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT>
                <button type="reset" value="重置" class="button_cancel">&#37325;&#32622;</button>

                <FONT face="宋体">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT>
                <INPUT class="button_ok" type="button" onclick="history.go(-1)" value="返回"/>
                <span id="Label1"></span>
            </td>
        </tr>
    </table>
</form>

<script src="js/jquery-1.11.3.min.js" type="application/javascript"></script>
<script src="js/vue.js" type="application/javascript"></script>
<script src="js/axios.min.js" type="application/javascript"></script>
<script>
    var vue = new Vue({
        el: "#Form1",
        data: {
            cid: 2,
            categoryList: "",
            product: {
                pname: "",
                marketPrice: 0,
                shopPrice: 0,
                pimage: "",
                isHot: 0,
                pdesc: "商品描述",
                pflag: 0,
                cid: '1',
            }
        },
        methods: {
            submit: function (event) {
                var ue_pdese = $("#ueditor_0").contents().find(".view>p").text();
                vue.product.pdesc = ue_pdese;

                axios({
                    url: "admin/productJsonServlet.action?method=addProduct",
                    method: "post",
                    data: vue.product,
                }).then(function (response) {
                    if (response.data.code == 1) {
                        alert("新增成功！" + response.data.message);
                        window.location.href = "admin/product/list.jsp"
                    } else {
                        alert("新增失败！" + response.data.message)
                    }
                });
            },
            uploadImage: function (event) {
                var formData = new FormData();
                formData.append("upload", event.target.files[0]);
                axios({
                    url: "admin/productJsonServlet.action?method=uploadImage",
                    data: formData,
                    method: "post",
                    headers: {
                        //指定以表单方式提交数据
                        'Content-Type': 'multipart/form-data'
                    }
                }).then(function (response) {
                    console.log("文件路径：" + response.data.pimage);
                    vue.product.pimage = response.data.pimage;
                });
            }
        },
        created: function () {
            //创建视图成功，则显示分类信息
            axios({
                url: "admin/productJsonServlet.action?method=categoryAll",
            }).then(function (response) {
                console.log(response.data);
                vue.categoryList = response.data;
            });
        }
    });
</script>
<script>
    $(function () {
        $(".view").blur(function () {
            alert("aa")
        });
        $("#pname").blur(function () {

            alert("aa:" + val);
        });
        $("#ueditor_0").contents().find(".view>p").bind("N]OMNodeInserted", function () {
            alert("aa");
        });
    })
</script>
</body>
</HTML>