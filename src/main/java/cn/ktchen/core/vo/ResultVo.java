package cn.ktchen.core.vo;

import lombok.Data;

/**
 * @ClassName ResultVo
 * $@Description 结果对象，用于返回信息,响应数据
 * @Author alex
 * Date 2019-02-22 00:40
 * Version 1.0
 **/
@Data
public class ResultVo<T> {
    // 状态码
    private Integer code;
    // 提示信息
    private String msg;
    //响应数据
    private T data;
}
