package cn.ktchen.core.utils.tool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JsonTool
 * @Description TODO
 * @Author alex
 * Date 2019-03-07 20:24
 * Version 1.0
 **/
public class JsonTool {
    private static ObjectMapper mapper = new ObjectMapper();

    // 对象解析成json字符串
    public static String toJson(Object object) {
        String json = null;
        try{
            json = mapper.writeValueAsString(object);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            return json;
        }
    }

    // json字符串解析成Map对象
    public static Map jsonToMap(String json) {
        Map map = new HashMap();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>(){};
        try {
            map = mapper.readValue(json, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return map;
        }
    }

    // json字符串解析成List对象
    public static List jsonToList(String json) {
        List list = new ArrayList();
        TypeReference<List> typeRef
                = new TypeReference<List>(){};
        try {
            list = mapper.readValue(json, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }

}
