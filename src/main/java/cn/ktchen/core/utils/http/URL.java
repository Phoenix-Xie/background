package cn.ktchen.core.utils.http;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName URL
 * $@Description 封装URL地址，自动添加应用上下文路径
 * @Author alex
 * Date 2019-02-22 00:59
 * Version 1.0
 **/
@Getter
@Setter
public class URL {
    private String url;
    public URL(){}

    /**
     *
     * @param url URL地址
     */
    public URL(String url) {
        this.url = HttpServletUtil.getRequest().getContextPath() + url;
    }
}
