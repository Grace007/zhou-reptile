package com.reptile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EmojiFilterUtils {
    /**
     *
     * @param source
     * @return 过滤后的字符串
     */

    /**********************************/
    public synchronized static String removeImoji(String source) {
        char[] chars = source.toCharArray();
        StringBuffer comment = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 0 && chars[i] <= 127)
                    || (chars[i] >= 12289 && chars[i] <= 12305) || (chars[i] >= 8212 && chars[i] <= 8230)
                    || (chars[i] >= 65281 && chars[i] <= 65311) || chars[i] == 65374) {
                comment.append(chars[i]);
            }
        }
        return comment.toString();
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？》～·《_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().replace("\\","");
    }

    public static void main(String[] arg) {
        String str = "a🏢🏣🏤🏥哈？！@#￥%…{}“：》？～~~·`《，。<>?:\\、、~~·~~{}|+_)(*&^%$#@!)…&*（）——+【】🏦🏧🏨?";
        System.out.println(removeImoji(str));
        System.out.println(StringFilter(str));
    }
}
