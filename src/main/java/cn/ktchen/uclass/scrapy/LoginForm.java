package cn.ktchen.uclass.scrapy;

import cn.ktchen.core.utils.common.Base64;
import cn.ktchen.uclass.util.ScoreUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @ClassName LoginForm
 * @Description 登录表单
 * @Author alex
 * Date 2019-03-04 22:14
 * Version 1.0
 **/
public class LoginForm {

    public static void login(String username, String password) throws IOException {
        String url = "http://jwgl.ouc.edu.cn/cas/logon.action";
        String jsessionid = getSession();
        String params = getParams(username, password, jsessionid);

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.REFERER, "http://jwgl.ouc.edu.cn/cas/login.action");
        headers.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8"));
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + jsessionid);
        HttpEntity<String> entity = new HttpEntity(params, headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
    }

    public static String getParams(String username, String password, String jsessionid) {
        String randnumber = "ppgh";
        password = ScoreUtil.md5(ScoreUtil.md5(password) + ScoreUtil.md5(randnumber));
        String p_username = "_u" + randnumber;
        String p_password = "_p" + randnumber;
        username = Base64.encode(username + ";;" + jsessionid);
        StringBuilder sb = new StringBuilder();
        sb.append(p_username);
        sb.append("=");
        sb.append(username);
        sb.append("&");
        sb.append(p_password);
        sb.append("=");
        sb.append(password);
        sb.append("&randnumber=");
        sb.append(randnumber);
        sb.append("&isPasswordPolicy=1");
        return sb.toString();
    }

    public static String getSession() {
//        RestTemplate template = new RestTemplate();
//        String url = "http://jwgl.ouc.edu.cn/cas/genValidateCode";
//        ResponseEntity<String> response = template.getForEntity(url, String.class);
//        List<String> cookies = response.getHeaders().get("Set-Cookie");
//        return cookies.get(0).substring(cookies.get(0).indexOf("=") + 1, cookies.get(0).indexOf("; "));
        return "D510B6104C8E8B62D8A033805A4B597C.kingo";
    }

    public static void main(String[] args) throws IOException {
        login("", "");
    }
}
