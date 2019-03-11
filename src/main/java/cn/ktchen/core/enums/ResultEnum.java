package cn.ktchen.core.enums;


import lombok.Getter;

/**
 * @ClassName ResultVoUtil
 * @Description 结果工具
 * @Author alex
 * Date 2019-02-22 00:47
 * Version 1.0
 **/
@Getter
public enum ResultEnum {
    SUCCESS(200, "success"),
    ERROR(400, "error");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
