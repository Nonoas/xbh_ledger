package indi.nonoas.xbh.utils;

import java.util.Calendar;

public class TimeUtil {

    private TimeUtil() {
    }

    /**
     * 偏移时间间隔
     * @param source 原时间戳
     * @param timeEnum 偏移单位
     * @param count 偏移数量
     * @return 偏移后的时间戳
     */
    public static long add(long source, TimeMilliEnum timeEnum, int count) {
        return source + timeEnum.milli * count;
    }

    public enum TimeMilliEnum{
        DAY(86400000);

        /**
         * 毫秒数
         */
        public final long milli;

        TimeMilliEnum(long milli){
            this.milli = milli;
        }
    }
}
