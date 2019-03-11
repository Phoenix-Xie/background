package cn.ktchen.uclass.controller;

import cn.ktchen.core.utils.http.ResultVoUtil;
import cn.ktchen.core.utils.tool.JsonTool;
import cn.ktchen.core.vo.ResultVo;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.websocket.server.PathParam;
import java.util.Map;

/**
 * @ClassName ClassController
 * @Description 课表控制器
 * @Author alex
 * Date 2019-03-08 00:05
 * Version 1.0
 **/
@RestController
@RequestMapping("/class")
public class ClassController {
    private static Jedis conn = new Jedis("123.206.79.131");
    static {
        conn.auth("alex1234");
    }

    @GetMapping
    public ResultVo getClass(@RequestParam(value = "y") int year,
                             @RequestParam(value = "s") int semster,
                             @RequestParam(value = "n") String classNum){
        String key = "class-" + year + "-" + semster;
        Map data = JsonTool.jsonToMap(conn.hget(key, "class:" + classNum));
        return ResultVoUtil.success(data);
    }

    @GetMapping("/error")
    public ResultVo error() {
        return ResultVoUtil.error();
    }
}
