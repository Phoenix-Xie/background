package cn.ktchen.core.utils.captcha;

import cn.ktchen.core.config.properties.CaptchaProperties;
import cn.ktchen.core.utils.common.CommonChineseWord;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName GifCaptcha
 * $@Description 动态验证码
 * @Author alex
 * Date 2019-02-20 15:13
 * Version 1.0
 **/
@Data
@Component
public class GifCaptcha {

    //验证码配置
    @Autowired
    private static CaptchaProperties properties;

    //字体
    private Font font = new Font("Default", Font.BOLD, 40);
    private int width = 200; // 验证码显示长度
    private int height = 80; // 验证码显示高度
    private String word = ""; // 当前的字符串
    private int delay = 100; // 帧延迟 (默认100)
    private int quality = 10;//量化器取样间隔 - 默认是10ms
    private int repeat = 0; // 帧循环次数
    private int minColor = 0;//设置随机颜色时，最小的取色范围
    private int maxColor = 255;//设置随机颜色时，最大的取色范围
    private int right = 0; //设置字符最右边的相对位置---相对原始位置 ，默认为0
    private int length = 4; //长度，
    //空构造函数
    public GifCaptcha(){};

    /**
     *
     * @param width 宽
     * @param height 高
     */
    public GifCaptcha(int width, int height){
        this.width = width;
        this.height = height;
    }

    /**
     *
     * @param width -验证码宽度
     * @param height -验证码高度
     * @param font -字体
     */
    public GifCaptcha(int width, int height,  Font font) {
        this(width, height);
        this.font = font;
    }

    /**
     * @param width -验证码宽度
     * @param height -验证码高度
     * @param font -字体
     * @param delay -帧延迟
     */
    public GifCaptcha(int width, int height, Font font,int delay) {
        this(width, height,font);
        this.delay = delay;
    }

    public GifCaptcha(int width, int height, Font font, int delay, int length){
        this(width, height, font, delay);
        this.length = length;
    }

    /**
     * 给定一个输出流 输入图片
     * @param os
     */
    public void out(OutputStream os) {
        try {
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();// gif编码类
            //生成字符
            gifEncoder.start(os);
            gifEncoder.setQuality(quality);//设置量化器取样间隔
            gifEncoder.setDelay(delay);//设置帧延迟
            gifEncoder.setRepeat(repeat);//帧循环次数
            BufferedImage frame;
            char[] rands = getWord().toCharArray();
            Color fontcolor[] = new Color[word.length()];
            for (int i = 0; i < word.length(); i++) {
                fontcolor[i] = getRandomColor(minColor,maxColor);
            }
            for (int i = 0; i < word.length(); i++) {
                frame = graphicsImage(fontcolor, rands, i);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 画随机码图
     *
     * @param fontcolor 随机字体颜色
     * @param strs      字符数组
     * @param flag      透明度使用
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //或得图形上下文
        Graphics2D g2d=image.createGraphics();
        //Graphics2D g2d = (Graphics2D) image.getGraphics();
        //利用指定颜色填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        AlphaComposite ac;
        float y = (height >> 1) + (font.getSize() >> 1) ;// 字符的y坐标
        float m = (width-(word.length()*font.getSize()))/word.length();
        float x = m/2;//字符的x坐标
        g2d.setFont(font);
        for (int i = 0; i < word.length(); i++) {
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getPellucidity(flag, i));
            g2d.setComposite(ac);
            g2d.setColor(fontcolor[i]);
            g2d.drawOval(Randoms.num(width), Randoms.num(height), Randoms.num(5,30), 5 + Randoms.num(5,30));//绘制椭圆边框
            g2d.drawString(strs[i] + "",x+(font.getSize()+m)*i+right,y);
        }
        g2d.dispose();
        return image;
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     * @return float 透明度
     */
    private float getPellucidity(int i, int j) {
        int num = i + j;
        float r = (float) 1 / word.length(), s = (word.length() + 1) * r;
        return num > word.length() ? (num * r - s) : num * r;
    }


    /**
     * 生成随机字符数组
     * @return 字符数组
     */
    public void createWordChar() {
        word = CommonChineseWord.getRandomWords(length);
    }

    /**
     * 通过给定范围获得随机的颜色
     * @return Color 获得随机的颜色
     */
    protected Color getRandomColor(int min, int max) {
        if (min > 255) {
            min = 255;
        }
        if (max > 255) {
            max = 255;
        }
        if(min<0){
            min=0;
        }
        if(max<0){
            max=0;
        }
        if(min>max){
            min=0;
            max=255;
        }
        return new Color(min + Randoms.num(max - min), min + Randoms.num(max - min), min + Randoms.num(max - min));
    }


}
