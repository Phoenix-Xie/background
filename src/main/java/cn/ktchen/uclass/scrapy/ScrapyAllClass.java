package cn.ktchen.uclass.scrapy;

import cn.ktchen.core.utils.tool.JsonTool;
import cn.ktchen.uclass.util.CreateFileUtil;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScrapyAllClass
 * @Description 抓取课表信息
 * @Author alex
 * Date 2019-03-07 11:54
 * Version 1.0
 **/
public class ScrapyAllClass {

    // 服务器redis，存放数据
    private static Jedis conn = new Jedis("123.206.79.131");
    static {
        conn.auth("alex1234");
    }

    // 课程表信息url
    private static final String TableUrl = "http://jwgl.ouc.edu.cn/taglib/DataTable.jsp?currPageCount=";
    private static final String Referer = "http://jwgl.ouc.edu.cn/student/wsxk.kcbcx.html?menucode=JW130414";
    private static RestTemplate restTemplate = new RestTemplate();

    // 专业代码 如计算机专业代码：0011
    private static String[] specifyCode = {"0003", "0005", "0006",
            "0009", "0010", "0011", "0012", "0013", "0014", "0015",
            "0016", "0017", "0018", "0019", "0020", "0023", "0024",
            "0025", "0026", "0027", "0030", "0031", "0032", "0033",
            "0034", "0035", "0036", "0037", "0038", "0039", "0040",
            "0041", "0042", "0043", "0044", "0045", "0047", "0048",
            "0049", "0050", "0051", "0052", "0053", "0055", "0056",
            "0057", "0058", "0059", "0060", "0061", "0062", "0064",
            "0065", "0072", "0077", "0087", "0088", "0089", "0091",
            "0092", "0095", "0096", "0097", "0099", "0100", "0101",
            "0102", "0103", "0104", "0105", "0106", "0107", "0108",
            "0131", "0132", "0133", "0134", "0135", "0136", "0137",
            "0138", "0139", "0140", "0141", "0142", "0143", "0144",
            "0146", "0147"};

    /**
     * 爬取通识课以及公共基础课信息
     * @param year  学年 如：2018
     * @param semester 学期 如：2
     * @param type 课程类别:(Common 通识课)(PublicBasic 公共基础课)
     * @param jesssionid 已登录sessionid
     */
    public static void scrapy(String year, String semester, String type, String jesssionid) {
        // headers
        HttpHeaders headers = getHeaders(jesssionid);
        // body
        MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
        data.add("xn", year);
        data.add("xq", semester);
        data.add("kcfw", type);
        data.add("tableId", "6146");
        HttpEntity<String> entity = new HttpEntity(data, headers);
        // request
        ResponseEntity<String> response = restTemplate.exchange(TableUrl, HttpMethod.POST, entity, String.class);
        // total page
        String html = response.getBody();
        int totalPage = getTotalPage(html);
        String key = "class-" + year + "-" + semester;
        List<Element> elementList = new ArrayList<>();
        // parse
        elementList.addAll(ClassFormParser.parseHtml(html));

        for (int i = 2; i <= totalPage; i++) {
            String url = TableUrl + i;
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            html = response.getBody();
            elementList.addAll(ClassFormParser.parseHtml(html));
        }

        sendServer(ClassFormParser.parseElements(elementList), key);
    }

    /**
     * 爬取专业课
     * @param year 学年
     * @param semester 学期
     * @param startYear 开始年份 如2015，则抓取2015-2018
     * @param jesssionid 已登录jsessionid
     */
    private static void scrapySpecialty(String year, String semester, int startYear, String jesssionid){
        int endYear = startYear + 3;
        for (int nowYear = startYear; nowYear <= endYear; nowYear++) {//年纪循环
            for (String code :
                    specifyCode) {
                // headers
                HttpHeaders headers = getHeaders(jesssionid);
                // body
                MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
                data.add("xn", year);
                data.add("xq", semester);
                data.add("kcfw", "Specialty");
                data.add("tableId", "6146");
                data.add("sel_zydm", code);
                data.add("sel_nj", Integer.toString(nowYear));

                HttpEntity<String> entity = new HttpEntity(data, headers);
                // request
                ResponseEntity<String> response = restTemplate.exchange(TableUrl, HttpMethod.POST, entity, String.class);
                // total page
                String html = response.getBody();
                int totalPage = getTotalPage(html);
                String key = "class-" + year + "-" + semester;
                List<Element> elementList = new ArrayList<>();
                // parse
                elementList.addAll(ClassFormParser.parseHtml(html));

                for (int i = 2; i <= totalPage; i++) {
                    String url = TableUrl + i;
                    response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                    html = response.getBody();
                    elementList.addAll(ClassFormParser.parseHtml(html));
                }
                sendServer(ClassFormParser.parseElements(elementList), key);
            }

        }
    }

    static StringBuilder sb2 = new StringBuilder();

    private static void sendServer(List<Map> classList, String key) {
        classList.forEach((oneclass)-> {
            sb2.append(JsonTool.toJson(oneclass) + "\n");
            System.out.println("课程：" + oneclass.get("className") + " 插入完毕");
        });
//        classList.forEach((oneClass) -> {
//            conn.hset(key, "class:" + oneClass.get("classNum"), JsonTool.toJson(oneClass));
//            System.out.println("课程：" + oneClass.get("className") + " 插入完毕");
//        });

    }

    /**
     * 获取头部
     * @param jesssionid
     * @return
     */
    private static HttpHeaders getHeaders(String jesssionid) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + jesssionid);
        headers.add(HttpHeaders.REFERER, Referer);
        return headers;
    }

    /**
     * 获取页面总数
     * @param html 任意课表页面
     * @return
     */
    public static int getTotalPage(String html) {
        int index = html.indexOf("DataTable.jsp") + 15;
        String[] data = html.substring(index, index+3).split(",");
        return Integer.parseInt(data[0]);
    }

    public static void main(String[] args) {
        String year = "2018";
        String semester = "2";
        String jsessionid = "2573C349DB77B1C84B75643AADCA4C58.kingo";
        int startYear = 2015;

//        // 专业课
//        scrapySpecialty(year, semester, startYear, jsessionid);
        // 通识课
        scrapy(year, semester, "Common", jsessionid);
        // 公共基础课
        scrapy(year, semester, "PublicBasic", jsessionid);

        CreateFileUtil.createJsonFile(sb2.toString(), "D:\\","class" );
    }

}
