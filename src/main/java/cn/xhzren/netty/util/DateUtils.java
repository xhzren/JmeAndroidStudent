package cn.xhzren.nettytest.connection.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public enum TranRule {
        YMD("yyyy-MM-dd"),
        YMDH("yyyy-MM-dd HH"),
        YMDHM("yyyy-MM-dd HH:mm"),
        YMDHMS("yyyy-MM-dd HH:mm:ss");

        private final String content;
        TranRule(String content) {
            this.content = content;
        }
    }

    public static String nowDateToString(TranRule tranRule) {
        SimpleDateFormat sdf = new SimpleDateFormat(tranRule.content);
        return sdf.format(new Date());
    }

}
