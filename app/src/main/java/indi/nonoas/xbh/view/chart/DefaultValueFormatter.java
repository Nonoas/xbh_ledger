package indi.nonoas.xbh.view.chart;


import android.text.TextUtils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * @author Nonoas
 * @date 2021-09-21 23:56
 */

public class DefaultValueFormatter implements IValueFormatter {

	/**
	 * DecimalFormat for formatting
	 */
	protected DecimalFormat mFormat;

	public DefaultValueFormatter() {
		setup("");
	}

	/**
	 * @param endDes 数字后添加的 单位
	 */
	public DefaultValueFormatter(String endDes) {
		setup(endDes);
	}

	private void setup(String endDes) {
		if (TextUtils.isEmpty(endDes)) {
			// ###.## 表示可以精确到小数点后两位，即 double 类型
			mFormat = new DecimalFormat("###.##");
		} else {
			mFormat = new DecimalFormat("###.##" + endDes);
		}
	}

	@Override
	public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
		return mFormat.format(value);
	}

	/**
	 * Returns the number of decimal digits this formatter uses.
	 *
	 * @return
	 */
	public int getDecimalDigits() {
		return 1;
	}
}
