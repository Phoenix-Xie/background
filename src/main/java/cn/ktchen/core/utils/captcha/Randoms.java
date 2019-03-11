package cn.ktchen.core.utils.captcha;

import java.util.Random;

/**
 * @ClassName Randoms
 * $@Description 产生随机数
 * @Author alex
 * Date 2019-02-20 15:39
 * Version 1.0
 **/
public class Randoms {
    private static final Random RANDOM = new Random();
    /**
     * 产生两个数之间的随机数
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max)
    {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 产生0--num的随机数,不包括num
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num)
    {
        return RANDOM.nextInt(num);
    }
}
