package cn.ktchen.core.utils.tool;

import java.util.Random;

/**
 * @ClassName RandomTool
 * @Description 获取随机字符串
 * @Author alex
 * Date 2019-02-23 17:18
 * Version 1.0
 **/
public class RandomTool {

    /**
     * 随机值
     */
    private static Character[] sands = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'g',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'G',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    /**
     * 获取指定长度的随机字符串
     * @param length
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int sandLength = sands.length;
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(sandLength);
            sb.append(sands[index]);
        }
        return sb.toString();
    }

}
