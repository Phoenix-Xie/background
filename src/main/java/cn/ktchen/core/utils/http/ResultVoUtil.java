package cn.ktchen.core.utils.http;

import cn.ktchen.core.enums.ResultEnum;
import cn.ktchen.core.vo.ResultVo;

/**
 * @ClassName ResultVoUtil
 * $@Description  响应数据工具
 * @Author alex
 * Date 2019-02-22 00:58
 * Version 1.0
 **/
public class ResultVoUtil {

    /**
     * 操作成功
     * @param msg 提示信息
     * @param object 返回对象
     */
    public static ResultVo success(String msg, Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg(msg);
        resultVo.setCode(ResultEnum.SUCCESS.getCode());
        resultVo.setData(object);
        return resultVo;
    }

    /**
     * 操作成功，返回url地址
     * @param msg 提示信息
     * @param url URL包装对象
     */
    public static ResultVo success(String msg, URL url) {
        return success(msg, url.getUrl());
    }

    /**
     * 操作成功，使用默认提示信息
     * @param object 返回对象
     */
    public static ResultVo success(Object object) {
        String message = ResultEnum.SUCCESS.getMessage();
        return success(message, object);
    }

    /**
     * 返回提示信息
     * @param msg 提示信息
     * @return
     */
    public static ResultVo success(String msg) {
        Object object = null;
        return success(msg, object);
    }

    /**
     * 操作成功，不返回数据
     * @return
     */
    public static ResultVo success(){
        return success("success");
    }

    /**
     *
     * @param code 错误吗
     * @param msg 提示信息
     * @param object 数据
     */
    public static ResultVo error(Integer code, String msg, Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        resultVo.setData(object);
        return resultVo;
    }

    /**
     *
     * @param msg
     * @param object
     * @return
     */
    public static ResultVo error(String msg, Object object) {
        Integer code = ResultEnum.ERROR.getCode();
        return error(code, msg, object);
    }

    /**
     * 错误
     * @param code 错误码
     * @param msg 提示信息
     */
    public static ResultVo error(Integer code, String msg) {
        return error(code, msg, null);
    }



    /**
     * 操作错误，返回默认400错误码
     * @param msg 提示信息
     */
    public static ResultVo error(String msg) {
        Integer code = ResultEnum.ERROR.getCode();
        return error(code, msg);
    }

    public static ResultVo error(Object object) {
        return error(ResultEnum.ERROR.getMessage(), object);
    }

    public static ResultVo error() {
        return error(ResultEnum.ERROR.getMessage());
    }
}
