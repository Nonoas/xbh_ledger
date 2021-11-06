package indi.nonoas.xbh.view.chart;

import android.annotation.SuppressLint;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.utils.DateTimeUtil;


/**
 * @author Nonoas
 * @date 2021-10-02 17:29
 */
public class DateValueFormatter extends ValueFormatter {

    private final String pattern;

    private final List<?> list;

    public DateValueFormatter(String pattern, List<?> list) {
        this.pattern = pattern;
        this.list = list;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if (index < 0 || index > list.size() - 1) {
            return "";
        }
        AccBalance balance = (AccBalance) list.get((int) value);
        long dateInt = balance.getDate();
        SimpleDateFormat fmSource = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        try {
            date = fmSource.parse(String.valueOf(dateInt));
        } catch (ParseException e) {
            Log.e(ILogTag.DEV, e.getMessage());
        }
        SimpleDateFormat fmTarget = new SimpleDateFormat(pattern);
        return fmTarget.format(date);
    }
}
