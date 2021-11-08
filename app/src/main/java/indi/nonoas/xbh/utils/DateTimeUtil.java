package indi.nonoas.xbh.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * 获取当前系统日期
     *
     * @return yyyyMMdd
     */
    public static int getCurrDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
     * 偏移时间间隔
     * @param source 原时间戳
     * @param timeEnum 偏移单位
     * @param count 偏移数量
     * @return 偏移后的时间戳
     */
    public static long add(long source, TimeMilliEnum timeEnum, int count) {
        return source + timeEnum.MILLI * count;
    }

    public enum TimeMilliEnum{
        DAY(86400000);

        /**
         * 毫秒数
         */
        public final long MILLI;

        TimeMilliEnum(long MILLI){
            this.MILLI = MILLI;
        }
    }
}
