package cn.ktchen.uclass.util;

import cn.ktchen.core.utils.common.Base64;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName ScoreUtil
 * @Descriptio 获取成绩所需的加密参数
 * @Author alex
 * Date 2019-03-04 17:38
 * Version 1.0
 **/
public class ScoreUtil {

    private static ScriptEngineManager manager = new ScriptEngineManager();
    private static ScriptEngine engine = manager.getEngineByName("javascript");
    private static Invocable invoke;

    static {
        String path = "classpath:static/js/des.js";
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(path);
        try {
            FileReader reader = new FileReader(resource.getFile());
            engine.eval(reader);
            invoke = (Invocable) engine;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key 密钥
     * @param data 数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String data) throws Exception {
        String result = null;
        result = (String)invoke.invokeFunction("strEnc", data, key);
        return result;
    }

    /**
     * md5哈希
     * @param data
     * @return
     */
    public static String md5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }

    /**
     * 获取token
     * @param params 原始参数
     * @param timestamp 时间戳
     * @return
     */
    public static String getToken(String params, String timestamp) {
        return md5(md5(params) + md5(timestamp));
    }

    /**
     * 从教务处获取deskey和timestamp加密参数
     */
    public static String[] getKeyAndTime(String cookies) {
        RestTemplate template = new RestTemplate();
        String url = "http://jwgl.ouc.edu.cn/custom/js/SetKingoEncypt.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookies);
        headers.add(HttpHeaders.REFERER, "http://jwgl.ouc.edu.cn/student/xscj.stuckcj.jsp?menucode=JW130705");
        HttpEntity<String> entity = new HttpEntity( headers);
        String jsp = template.exchange(url, HttpMethod.GET, entity, String.class).toString();
        String[] splice = jsp.split("\n");
        String[] result = new String[2];
        String deskeyLine = splice[2];
        String nowtime = splice[3].substring(16, 35);
        String deskey = deskeyLine.substring(15, 34);
        result[0] = nowtime;
        result[1] = deskey;
        return result;
    }

    public static String getParams(String account, String cookies) throws Exception{
        String params = "xn=2018&xn1=2019&xq=2&ysyx=yscj&sjxz=sjxz1&userCode=" + account + "&ysyxS=on&sjxzS=on";
        StringBuilder sb = new StringBuilder();
        String[] keys = getKeyAndTime(cookies);
        sb.append("params=");
        sb.append(Base64.encode(encrypt(keys[1], params)));
        sb.append("&token=");
        sb.append(getToken(params, keys[0]));
        sb.append("&timestamp=");
        sb.append(keys[0]);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        String key = "5431551704925566630";
        String time = "2019-03-04 21:08:45";
        String params = "xn=2018&xn1=2019&xq=2&ysyx=yscj&sjxz=sjxz1&userCode=17020031002&ysyxS=on&sjxzS=on";
        StringBuilder sb = new StringBuilder();
        sb.append("params=");
        sb.append(Base64.encode(encrypt(key, params)));
        sb.append("&token=");
        sb.append(getToken(params, time));
        sb.append("&timestamp=");
        sb.append(time);
        System.out.println(sb);
    }
}
