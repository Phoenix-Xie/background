package cn.ktchen.uclass.except;


import cn.ktchen.core.utils.http.ResultVoUtil;
import cn.ktchen.core.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ExceptionHandler
 * @Description TODO
 * @Author alex
 * Date 2019-03-08 00:47
 * Version 1.0
 **/
@ControllerAdvice
public class ResultExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResultVo runtimeException(RuntimeException e) {
        return ResultVoUtil.error();
    }
}
