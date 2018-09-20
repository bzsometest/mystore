package com.bzchao.mystore.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件的工具类:
 *
 * @author admin
 */
public class MailUtils {
    private static final String USERNAME = "228953280@qq.com";
    private static final String PASSWORD = "uugnowgbesdkbhjb";

    public static void sendMail(String toMail, String content) {
        try {
            // 获得连接:
            Properties props = new Properties();
            props.setProperty("mail.debug", "true"); //输出debug信息
            props.setProperty("mail.host", "smtp.qq.com");//邮件服务器
            props.setProperty("mail.transport.protocol", "smtp"); //协议
            props.setProperty("mail.smtp.auth", "true"); //用户验证

            //邮箱授权信息
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            };

            Session session = Session.getInstance(props, authenticator);

            // 构建邮件:
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            // 设置收件人:
            // TO:收件人   CC:抄送   BCC:暗送,密送.
            message.addRecipient(RecipientType.TO, new InternetAddress(toMail));
            // 主题:
            message.setSubject("来自天虎官方商城的激活邮件!");
            // 正文:
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件:
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String code = "asdsadds";

        String content = "<h1>来自天虎官方商城的激活邮件:请点击下面链接激活!</h1><h3><a href='http://localhost:8888/userServlet?method=active&code=" + code + "'>http://localhost:8888/userServlet?method=active&code=" + code + "</a></h3>";
        MailUtils.sendMail("153355720@qq.com", content);
    }
}
