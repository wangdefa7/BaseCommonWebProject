package com.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *工具类XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
public class Util {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 去除中括号
     */
    public static String removeBrackets(String s) {
        s = s.replace("[", "");
        s = s.replace("]", "");
        return s;
    }
    /**
     * 去除空格
     */
    public static String removeBlank(String s) {
        s = s.replace(" ", "");
        return s;
    }

    /**
     * 日期后退一天
     * @param dt
     * @return String
     */
    public static String getTime(String dt){
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, +1);
        return sdf.format(c.getTime());//日期后退一天
    }

}
