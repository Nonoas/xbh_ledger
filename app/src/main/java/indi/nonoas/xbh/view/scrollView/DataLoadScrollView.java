package indi.nonoas.xbh.view.scrollView;

import android.content.Context;
import android.util.AttributeSet;

import indi.nonoas.xbh.view.IDataLoadView;

/**
 * 弹性ScrollView
 *
 * @author Nonoas
 * @date 2021/11/21
 */
public class DataLoadScrollView extends FlexibleScrollView implements IDataLoadView {

    public DataLoadScrollView(Context context) {
        super(context);
    }

    public DataLoadScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onLoadSuccess() {

    }
}
