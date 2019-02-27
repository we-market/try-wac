package com.wemarket.wac.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameUtil {
    /**
     * realName正则判断姓名是否合法
     */
    public static boolean isRealName(String realName){
        String expression = "([\u2E80-\uFE4F]|·){2,16}";
        //String expression = "([\u4e00-\u9fa5]|·){2,16}";
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(realName); // 操作的字符串
        boolean b = m.matches(); //返回是否匹配的结果
        if(b){
            expression = "[^、。【】《》]*";
            p = Pattern.compile(expression);
            m = p.matcher(realName);
            b = m.matches();
        }
        return b;
    }
}
