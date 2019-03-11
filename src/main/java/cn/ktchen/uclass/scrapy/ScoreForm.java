package cn.ktchen.uclass.scrapy;

import cn.ktchen.uclass.util.ScoreUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName ScoreForm
 * @Description 抓取成绩
 * @Author alex
 * Date 2019-03-04 19:46
 * Version 1.0
 **/
public class ScoreForm {

    public static Map getForm(String account, String cookies) throws Exception{
        RestTemplate template = new RestTemplate();

        String url = "http://jwgl.ouc.edu.cn:80/student/xscj.stuckcj_data.jsp?"
                        + ScoreUtil.getParams(account, cookies);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookies);
        headers.add(HttpHeaders.REFERER, "http://jwgl.ouc.edu.cn/student/xscj.stuckcj.jsp?menucode=JW130705");
        HttpEntity<String> entity = new HttpEntity( headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
        return null;
    }

    public static void parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        // TODO 解析成绩页面
    }

    public static void main(String[] args) throws Exception{
        String account = "17020031002";
        String cookies = "LOGIN=3137303230303331303032; SCREEN_NAME=4d6368656830636c696166333941684f6350397262673d3d; UM_distinctid=168331986083e7-0cd74863fc717c-10306653-13c680-1683319860938c; JSESSIONID=D510B6104C8E8B62D8A033805A4B597C.kingo";
        getForm(account, cookies);
    }
}
