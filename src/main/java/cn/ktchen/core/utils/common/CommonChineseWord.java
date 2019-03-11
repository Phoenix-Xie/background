package cn.ktchen.core.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName CommonChineseWord
 * $@Description 常见汉字
 * @Author alex
 * Date 2019-02-20 16:04
 * Version 1.0
 **/
public class CommonChineseWord {
    private static List<Character> words = new ArrayList<>();
    private static StringBuilder stringBuilder = new StringBuilder();

    static {
        stringBuilder.append("的一是了我不人在他有这个上")
                .append("到时大地为子中你说生国年着就那和要她出也得里后自以会家可下而过")
                .append("天去能对小多然于心学么之都好看起发当没成只如事把还用第样道想作")
                .append("种开美总从无情己面最女但现前些所同日手又行意动方期它头经长儿回")
                .append("位分爱老因很给名法间斯知世什两次使身者被高已亲其进此话常与活正")
                .append("感见明问力理尔点文几定本公特做外孩相西果走将月十实向声车全信重")
                .append("三机工物气每并别真打太新比才便夫再书部水像眼等体却加电主界门利")
                .append("海受听表德少克代员许稜先口由死安写性马光白或住难望教命花结乐色")
                .append("奇管类未朋且婚台夜青北队久乎越观落尽形影红爸百令周吧识步希亚术")
                .append("留市半热送兴造谈容极随演收首根讲整式取照办强石古华拿计您装似来")
                .append("足双妻尼转诉米称丽客南领节衣站黑刻统断福城故历惊脸选包紧争另建")
                .append("维绝树系伤示愿持千史谁准联妇纪基买志静阿诗独复痛消社算算义竟确")
                .append("酒需单治卡幸兰念举仅钟怕共毛句息功官待究跟穿室易游程号居考突皮")
                .append("哪费倒价图具刚脑永歌响商礼细专黄块脚味灵改据般破引食仍存众注笔")
                .append("甚某沉血备习校默务土微娘须试怀料调广们苏显赛查密议底列富梦错座");

        String str_words = stringBuilder.toString();

        for (int i = 0, length = str_words.length(); i < length; i++) {
            words.add(str_words.charAt(i));
        }
    }

    /**
     * 获取随机单个常见汉字
     *
     * @return
     */
    public static Character getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    /**
     * 获取指定长度随机汉字
     *
     * @param length 长度
     * @return
     */
    public static String getRandomWords(int length) {
        if (length <= 0) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(getRandomWord());
            }
            return builder.toString();
        }
    }
}
