package cn.ktchen.uclass.scrapy;

import cn.ktchen.core.utils.tool.JsonTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @ClassName ClassFormParser
 * @Description 解析课表信息
 * @Author alex
 * Date 2019-03-04 14:15
 * Version 1.0
 **/
public class ClassFormParser {
    /**
     * 解析html文本,获取课表信息
     * @param html
     * @return
     */
    public static List parseHtml(String html) {
        List<Map> classes = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("tbody");
        elements = elements.select("tr");
        return elements;
    }

    public static List parseElements(List<Element> elements) {
        List<Element> elementList = new ArrayList<>();
        List<Map> classes = new ArrayList<>();
        // 解析课表
        for (int i = 0; i < elements.size(); i++) {
            Element row = elements.get(i).child(0);
            String classNum = row.text().trim();
            if (StringUtils.isEmpty(classNum)) {
                // 课程号为空，加入队伍
                elementList.add(elements.get(i));
            }else {
                // 课程号不为空，解析课表，创建新队伍
                Map oneClass = parseClass(elementList);
                if (oneClass != null) {
                    classes.add(oneClass);
                }
                elementList = new ArrayList<>();
                elementList.add(elements.get(i));
            }
        }
        // 将最后一个队伍传递出去
        Map oneClass = parseClass(elementList);
        if (oneClass != null) {
            classes.add(oneClass);
        }
        return classes;
    }


    private static Map parseClass(List<Element> rows) {
        if (rows == null || rows.size() == 0){
            return null;
        }

        // 获取基本信息
        Map classInfo = parseRow(rows.get(0));
        // 获取时间、地点、备注
        List<Map> times = new ArrayList();
        for (int i = 0, size = rows.size(); i < size; i++) {
            // 获取课程时间
            Map nowTime = parseClassTime(rows.get(i));

            boolean canMerge = false;
            // 时间、地点一致则合并周次
            for (Map time :
                    times) {
                if (canMergeWeek(time, nowTime)){
                    // 合并
                    Set weeks = (Set)time.get("weeks");
                    weeks.addAll((Set)nowTime.get("weeks"));
                    // 备注一般后者更完整
                    time.put("remark", nowTime.get("remark"));
                    canMerge = true;
                    break;
                }
            }
            // 时间、周次一致则合并地点
            if (!canMerge){
                for (Map time:
                        times) {
                    if (canMergeClassRoom(time, nowTime)){
                        Set classRoom = (Set)time.get("classRoom");
                        classRoom.addAll((Set)nowTime.get("classRoom"));
                        time.put("remark", nowTime.get("remark"));
                        canMerge = true;
                        break;
                    }
                }
            }
            // 周次和地点不能合并则加入队伍
            if (!canMerge){
                times.add(nowTime);
            }
        }
        classInfo.put("time", times);
        return classInfo;
    }

    /**
     * 根据时间和周次判断是否可以合并地点
     * @param time
     * @param nowTime
     * @return
     */
    private static boolean canMergeClassRoom(Map time, Map nowTime) {

        // 判断周次
        Set<Integer> weeks1 = (Set)time.get("weeks");
        Set<Integer> weeks2 = (Set)nowTime.get("weeks");
        if (weeks1.size() != weeks2.size() || !weeks1.containsAll(weeks2)) {
            return false;
        }

        // 判断时间
        List<Integer> lessons1 = (List)time.get("lessons");
        List<Integer> lessons2 = (List<Integer>) nowTime.get("lessons");
        if (!lessons1.containsAll(lessons2) && !time.get("day").equals(nowTime.get("day"))){
            return false;
        }

        return true;
    }

    /**
     * 判断两条数据的周次是否可以合并，根据每周上课时间、地点判断
     * @param firstRow timeMap1
     * @param secondRow timeMap2
     */
    private static boolean canMergeWeek(Map firstRow, Map secondRow) {

        // 星期不一致
        if (!firstRow.get("day").equals(secondRow.get("day"))){
            return false;
        }

        // 地点不一致
        if (!firstRow.get("classRoom").equals(secondRow.get("classRoom"))) {
            return false;
        }

        // 上课时间不一致
        List<Integer> lessons1 = (List)firstRow.get("lessons");
        List<Integer> lessons2 = (List)secondRow.get("lessons");
        if (lessons1.size() != lessons2.size() || lessons1.containsAll(lessons2)){
            return false;
        }
        return true;
    }

    /**
     * 解析完整的一行课程数据，不包括时间，地点，备注
     * @param row
     * @return
     */
    private static Map parseRow(Element row) {
        Map<String, Object> rowClass = new HashMap();
        rowClass.put("classNum", row.child(0).text().trim());                       // 课程号
        rowClass.put("classCode", parseClassCode(row.child(1).text().trim()));      // 课程代码
        rowClass.put("className", parseClassName(row.child(1).text().trim()));      // 课程名
        rowClass.put("campus", row.child(2).text().trim());                         // 校区
        rowClass.put("teachers", row.child(5).text().trim().split(" "));      // 教师
        rowClass.put("credit", row.child(7).text().trim());                         // 学分
        return rowClass;
    }

    private static String parseClassName(String classText) {
        int startIndex = classText.indexOf(']') + 1;
        return classText.substring(startIndex);
    }

    private static String parseClassCode(String classText) {
        int startIndex = classText.indexOf('[') + 1;
        int endIndex = classText.indexOf(']');
        return classText.substring(startIndex, endIndex);
    }

    /**
     * 解析一节课
     * @param row 课程数据
     */
    private static Map parseClassTime(Element row) {
        String time = row.child(12).text().trim();
        Set weeks = parseClassWeeks(time);          // 周次
        List lessons = parseLessons(time);       // 每天的第几节课
        Character dayOfWeek = parseClassDay(time);     // 上课时间 星期*
        Set<String> classRoom = new HashSet<>();
        classRoom.add(row.child(13).text().trim());

        Map<String, Object> classTime = new HashMap<>();
        classTime.put("weeks", weeks);
        classTime.put("lessons", lessons);
        classTime.put("day", dayOfWeek);
        classTime.put("classRoom", classRoom);
        classTime.put("remark", row.child(18).text().trim());
        return classTime;
    }

    /**
     * 解析周次
     * @param time
     * @return
     */
    private static Set parseClassWeeks(String time) {
        Set<Integer> weeks = new HashSet<>();    // 上课周次
        // 解析周次,以"周"分隔开
        String weekInfo = time.substring(0, time.indexOf("周"));
        String[] weekStr = weekInfo.split("-");
        int startWeek = 0;
        int endWeek = 0;
        try {
            startWeek = Integer.parseInt(weekStr[0]);
            endWeek = Integer.parseInt(weekStr[weekStr.length-1]);
        }catch (Exception e) {
            e.printStackTrace();
        }


        // 判断是否单双周
        char flag = time.charAt(time.length() - 1);
        int offset = 1;
        if (flag == '单') {
            offset = 2;
            startWeek += ~(startWeek%2) + 2;
        }
        if (flag == '双') {
            offset = 2;
            startWeek += (startWeek %2);
        }
        for (int nowWeek = startWeek; nowWeek <= endWeek; nowWeek+=offset) {
            weeks.add(nowWeek);
        }
        return weeks;
    }

    /**
     * 解析上第几节课 如1-2为上第1、2节课
     * @param time
     * @return
     */
    private static List parseLessons(String time) {
        int startIndex = time.indexOf("(") + 1;
        int endIndex = time.indexOf("节");
        List<Integer> lessons = new ArrayList<>();
        if (startIndex == -1 || endIndex == -1){
            return lessons;
        }
        String[] nums = time.substring(startIndex, endIndex).split("-");
        int start = Integer.parseInt(nums[0]);
        int end = Integer.parseInt(nums[nums.length -1]);
        for (int now = start; now <= end; now++) {
            lessons.add(now);
        }
        return lessons;
    }

    /**
     * 解析上星期几的课
     * @param time
     * @return
     */
    private static Character parseClassDay(String time) {
        int startIndex = time.indexOf("周") + 2;
        Character day = new Character(' ');
        try {
            day = time.charAt(startIndex);
        }catch (IndexOutOfBoundsException e){
        }
        return day;
    }



    /**
     * 测试 获取本地课表
     */
    public static String getLocalClassForm(){
        ResourceLoader loader = new DefaultResourceLoader();
        String path = "templates/classform.html";
        Resource resource = loader.getResource(path);
        String content = null;
        try {
            content = new Scanner(resource.getInputStream(), "GB2312").useDelimiter("\\Z").next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main(String[] args)
    {
       parseHtml(getLocalClassForm());
    }

}
