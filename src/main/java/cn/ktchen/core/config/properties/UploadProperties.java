package cn.ktchen.core.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName FileProperties
 * @Description 上传文件属性
 * @Author alex
 * Date 2019-02-27 00:06
 * Version 1.0
 **/
@Component(value = "uploadProperties")
@ConfigurationProperties(prefix = "project.file")
public class UploadProperties {


    // 本地文件上传开关
    private static boolean open;

    // 上传文件路径
    private static String upload;

    // 限制文件大小
    private static Long MAX_FILE_SIZE;

    public static boolean isOpen() {
        return open;
    }

    public static String getUpload() {
        return upload;
    }

    public static Long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    // 默认开启
    @Value("${project.file.open:true}")
    public void setOpen(boolean open) {
        UploadProperties.open = open;
    }

    // 默认上传路径 /upload
    @Value("${project.file.upload:/upload}")
    public void setUpload(String upload) {
        UploadProperties.upload = upload;
    }

    // 默认限制文件大小 10M
    @Value("${project.file.maxFileSize:10485760}")
    public void setMaxFileSize(Long maxFileSize) {
        MAX_FILE_SIZE = maxFileSize;
    }
}
