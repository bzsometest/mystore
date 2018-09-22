package com.bzchao.mystore.web;

import com.bzchao.mystore.entity.Order;
import com.bzchao.mystore.service.impl.OrderServiceImpl;
import com.bzchao.mystore.utils.PaymentUtil;
import com.bzchao.mystore.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/playServlet.action")
public class PlayServlet extends BaseServlet {
    /**
     * 用户支付订单
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String playOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");

        String p0_Cmd = "Buy";
        String p1_MerId = "10001126856";// 真实
        String p2_Order = oid;
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        String p8_Url = ServletUtils.getWebPath(req) + "/playServlet.action?method=playBack";
        String p9_SAF = "";
        String pa_MP = "";
        String pd_FrpId = "CCB-NET-B2C";
        String pr_NeedResponse = "";

        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur,
                p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId,
                pr_NeedResponse, keyValue);

        StringBuilder url = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node?");
        url.append("p0_Cmd=").append("Buy");
        url.append("&p1_MerId=").append(p1_MerId);
        url.append("&p2_Order=").append(p2_Order);
        url.append("&p3_Amt=").append(p3_Amt);
        url.append("&p4_Cur=").append(p4_Cur);
        url.append("&p5_Pid=").append(p5_Pid);
        url.append("&p6_Pcat=").append(p6_Pcat);
        url.append("&p7_Pdesc=").append(p7_Pdesc);
        url.append("&p8_Url=").append(p8_Url);
        url.append("&p9_SAF=").append(p9_SAF);
        url.append("&pa_MP=").append(pa_MP);
        url.append("&pd_FrpId=").append(pd_FrpId);
        url.append("&pr_NeedResponse=").append(pr_NeedResponse);
        url.append("&hmac=").append(hmac);


        return REDIRECT + url;
    }

    /**
     * 支付回调接口
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String playBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //编程方式：正逻辑流程,使用if对错误逻辑进行处理

        //获取参数
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");

        //验证hmac是否正确
        // hmac
        String hmac = request.getParameter("hmac");
        // 利用本地密钥和加密算法 加密数据
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        //数据校验失败
        if (!isValid) {
            //提示错误信息
            request.setAttribute("message", "数据校验失败，系统已记录错误信息。");
            //提前返回，不在处理数据
            return "info";
        }

        //更新数据库
        handlerPlaySuccess(r6_Order);

        //判断通知类型(点对点|重定向)
        if (r9_BType.equals("1")) {
            //跳转页面，提示用户支付成功！
            request.setAttribute("message", "支付成功！");
        } else {
            //回复易宝 success
            response.getWriter().write("success");
            response.getWriter().flush();
            return null;
        }

        return "info";
    }

    /**
     * 处理支付成功
     * 非Servlet方法，禁止用户直接调用此方法
     *
     * @param oid
     */
    private void handlerPlaySuccess(String oid) throws ServletException, IOException {
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = orderService.findByOidSimple(oid);
        order.setState(1);
        orderService.update(order);
    }

}
