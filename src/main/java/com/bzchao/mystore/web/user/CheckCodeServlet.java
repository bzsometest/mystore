package com.bzchao.mystore.web.user;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 生成随机图片，用来作为验证码
 * 删除生成汉字验证码
 * 调节验证码宽度，将不会导致显示不完整
 */
@WebServlet("/checkCode.action")
public class CheckCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 3038623696184546092L;

    public static final String CHECK_CODE_NAME = "checkCode";
    public static final int WIDTH = 120;//生成的图片的宽度
    public static final int HEIGHT = 30;//生成的图片的高度

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String createTypeFlag = request.getParameter("createTypeFlag");//接收客户端传递的createTypeFlag标识
        //1.在内存中创建一张图片
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //2.得到图片
        Graphics g = bi.getGraphics();
        //3.设置图片的背影色
        setBackGround(g);
        //4.设置图片的边框
        setBorder(g);
        //5.在图片上画干扰线
        drawRandomLine(g);
        //6.写在图片上随机数
        //String random = drawRandomNum((Graphics2D) g,"nl");//生成数字和字母组合的验证码图片
        //String random = drawRandomNum((Graphics2D) g,"n");//生成纯数字的验证码图片
        //String random = drawRandomNum((Graphics2D) g,"l");//生成纯字母的验证码图片
        String random = drawRandomNum((Graphics2D) g, createTypeFlag);//根据客户端传递的createTypeFlag标识生成验证码图片
        //7.将随机数存在session中
        request.getSession().setAttribute(CHECK_CODE_NAME, random);
        //8.设置响应头通知浏览器以图片的形式打开
        response.setContentType("image/jpeg");//等同于response.setHeader("Content-Type", "image/jpeg");
        //9.设置响应头控制浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        //10.将图片写给浏览器
        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    /**
     * 设置图片的背景色
     *
     * @param g
     */
    private void setBackGround(Graphics g) {
        // 设置颜色
        g.setColor(Color.WHITE);
        // 填充区域
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * 设置图片的边框
     *
     * @param g
     */
    private void setBorder(Graphics g) {
        // 设置边框颜色
        g.setColor(Color.BLUE);
        // 边框区域
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
    }

    /**
     * 在图片上画随机线条
     *
     * @param g
     */
    private void drawRandomLine(Graphics g) {
        // 设置颜色
        g.setColor(Color.GREEN);
        // 设置线条个数并画线
        for (int i = 0; i < 5; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            int x2 = new Random().nextInt(WIDTH);
            int y2 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 画随机字符
     *
     * @param g
     * @param createTypeFlag
     * @return String... createTypeFlag是可变参数，
     * Java1.5增加了新特性：可变参数：适用于参数个数不确定，类型确定的情况，java把可变参数当做数组处理。注意：可变参数必须位于最后一项
     */
    private String drawRandomNum(Graphics2D g, String... createTypeFlag) {
        // 设置颜色
        g.setColor(Color.RED);
        // 设置字体
        g.setFont(new Font("宋体", Font.BOLD, 20));
        //数字和字母的组合
        String baseNumLetter = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
        //纯数字
        String baseNum = "0123456789";
        //纯字母
        String baseLetter = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
        //createTypeFlag[0]==null表示没有传递参数
        if (createTypeFlag.length > 0 && null != createTypeFlag[0]) {
            if (createTypeFlag[0].equals("nl")) {
                // 截取数字和字母的组合
                return createRandomChar(g, baseNumLetter);
            } else if (createTypeFlag[0].equals("n")) {
                // 截取数字
                return createRandomChar(g, baseNum);
            } else if (createTypeFlag[0].equals("l")) {
                // 截取字母
                return createRandomChar(g, baseLetter);
            }
        } else {
            // 默认截取数字和字母的组合
            return createRandomChar(g, baseNumLetter);
        }

        return "";
    }

    /**
     * 创建随机字符
     *
     * @param g
     * @param baseChar
     * @return 随机字符
     */
    private String createRandomChar(Graphics2D g, String baseChar) {
        StringBuffer sb = new StringBuffer();
        int x = 5;
        String ch = "";
        // 控制字数
        for (int i = 0; i < 4; i++) {
            // 设置字体旋转角度
            int degree = new Random().nextInt() % 30;
            ch = baseChar.charAt(new Random().nextInt(baseChar.length())) + "";
            sb.append(ch);
            // 正向角度
            g.rotate(degree * Math.PI / 180, x, 20);
            g.drawString(ch, x, 20);
            // 反向角度
            g.rotate(-degree * Math.PI / 180, x, 20);
            x += (WIDTH - 10) / 4;
        }
        return sb.toString();
    }
}
