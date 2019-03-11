package cn.ktchen.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName CaptchaProperties
 * $@Description 验证码配置
 * @Author alex
 * Date 2019-02-20 16:34
 * Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "project.captcha")
public class CaptchaProperties {

    // 是否开启验证码,默认关闭
    private boolean open = false;

    // 验证码长度，默认4
    private Integer length = 4;

}
