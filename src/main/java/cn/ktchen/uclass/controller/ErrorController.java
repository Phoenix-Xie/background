package cn.ktchen.uclass.controller;

import cn.ktchen.core.utils.http.ResultVoUtil;
import cn.ktchen.core.vo.ResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ErrorController
 * @Description TODO
 * @Author alex
 * Date 2019-03-08 00:54
 * Version 1.0
 **/
@RestController
@RequestMapping("/error")
public class ErrorController {

    @GetMapping
    public ResultVo error() {
        return ResultVoUtil.error();
    }
}
