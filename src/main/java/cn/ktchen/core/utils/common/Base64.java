package cn.ktchen.core.utils.common;

import org.springframework.util.Base64Utils;

/**
 * @ClassName Base64
 * @Description base64编码、解码
 * @Author alex
 * Date 2019-03-04 16:25
 * Version 1.0
 **/
public class Base64 {
    public static String encode(String data) {
        byte[] encodedBytes = Base64Utils.encode(data.getBytes());
        return new String(encodedBytes);
    }

    public static String decode(String data)  {
        byte[] decodeBytes = Base64Utils.decode(data.getBytes());
        return new String(decodeBytes);
    }
}
