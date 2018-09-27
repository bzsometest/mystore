<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<HTML>
<HEAD>
    <base href="${pageContext.request.contextPath}/">
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        function addProduct() {
            window.location.href = "${pageContext.request.contextPath}/admin/product/add.jsp";
        }
    </script>
</HEAD>
<body>
<br>
<form id="Form1" name="Form1">
    <table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
        <TBODY>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3">
                <strong>商品列表</strong>
            </TD>
        </tr>
        <tr>
            <td class="ta_01" align="right">
                <button type="button" id="add" name="add" value="添加" class="button_add" onclick="addProduct()">
                    &#28155;&#21152;
                </button>

            </td>
        </tr>
        <tr>
            <td class="ta_01" align="center" bgColor="#f5fafe">
                <table cellspacing="0" cellpadding="1" rules="all"
                       bordercolor="gray" border="1" id="DataGrid1"
                       style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
                    <tr
                            style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

                        <td align="center" width="18%">
                            序号
                        </td>
                        <td align="center" width="17%">
                            商品图片
                        </td>
                        <td align="center" width="17%">
                            商品名称
                        </td>
                        <td align="center" width="17%">
                            商品价格
                        </td>
                        <td align="center" width="17%">
                            是否热门
                        </td>
                        <td width="7%" align="center">
                            编辑
                        </td>
                        <td width="7%" align="center">
                            下架
                        </td>
                    </tr>

                    <tr v-for="product in pageBean.list" onmouseover="this.style.backgroundColor = 'white'"
                        onmouseout="this.style.backgroundColor = '#F5FAFE';">

                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="18%">
                            {{product.pid}}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="17%">
                            <img width="40" height="45" :src="product.pimage">

                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="17%">
                            {{ product.pname }}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="17%">
                            {{ product.shopPrice}}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center" width="17%">
                            <span v-if="product.isHot == 1">
                                是
                            </span>
                            <span v-else>
                                否
                            </span>

                        </td>

                        <td align="center" style="HEIGHT: 22px">
                            <a href="${pageContext.request.contextPath}/admin/productServlet.action?method=editUI&pid=${product.pid}">
                                <img src="${pageContext.request.contextPath}/images/i_edit.gif" border="0"
                                     style="CURSOR: hand">
                            </a>
                        </td>

                        <td align="center" style="HEIGHT: 22px">
                            <a href="${pageContext.request.contextPath}/admin/productServlet.action?method=pushDown&pid=${product.pid}">
                                <img src="${pageContext.request.contextPath}/images/i_del.gif" width="16"
                                     height="16" border="0" style="CURSOR: hand">
                            </a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr align="center">
            <td colspan="7">
                第{{ pageBean.currPage }} / {{ pageBean.totalPage }} 页 &nbsp; &nbsp; &nbsp;
                总记录数:{{ pageBean.totalCount }} &nbsp; 每页显示: {{ pageBean.pageSize }}

                <span v-if="pageBean.currPage > 1">
                    <a href="javascript:;" @click="pageTo(1)">首页</a>|
                    <a href="javascript:;" @click="pageTo(pageBean.currPage + 1)">上一页</a>|
                </span>

                &nbsp; &nbsp;

                <span v-for="currPage in pageBean.totalPage">
                     <a href="javascript:;" @click="pageTo(currPage)"> [{{ currPage }}] </a>
                </span>

                &nbsp; &nbsp;

                <span v-if="pageBean.currPage < pageBean.totalPage">
                    <a href="javascript:;" @click="pageTo(pageBean.currPage + 1)">下一页</a>|
                    <a href="javascript:;" v-on:click="pageTo(pageBean.totalPage)">尾页</a>|
                </span>

            </td>
        </tr>
        </TBODY>
    </table>
</form>

<script src="js/jquery-1.11.3.min.js" type="application/javascript"></script>
<script src="js/axios.min.js" type="application/javascript"></script>
<script src="js/vue.js" type="application/javascript"></script>

<script type="application/javascript">
    var vue = new Vue({
        el: "#Form1",
        data: {
            pageBean: ""
        },
        methods: {
            pageTo: function (page) {
                axios({
                    url: "admin/productJsonServlet.action?method=productByPage&count=10&page=" + page,
                }).then(function (response) {
                    vue.pageBean = response.data;
                });
            }
        },
        created: function () {
            //这里必须使用this，不能使用对象名，对象还未初始化完成
            this.pageTo(1);
        }
    });
</script>
</body>
</HTML>

