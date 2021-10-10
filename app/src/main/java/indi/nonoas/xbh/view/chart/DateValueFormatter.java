package indi.nonoas.xbh.view.chart;

import android.annotation.SuppressLint;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.utils.TimeUtil;


/**
 * @author Nonoas
 * @date 2021-10-02 17:29
 */
public class DateValueFormatter extends ValueFormatter {

    private final String pattern;
    private final long currTime;

    public DateValueFormatter(String pattern,long startTimeStamp) {
        this.pattern = pattern;
        currTime = startTimeStamp;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        long time = TimeUtil.add(currTime, TimeUtil.TimeMilliEnum.DAY, (int) value);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Log.d(ILogTag.DEV, "格式化：" + format.format(time));
        return format.format(time);
    }
}
