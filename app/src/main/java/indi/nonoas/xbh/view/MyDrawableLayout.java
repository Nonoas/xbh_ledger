package indi.nonoas.xbh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import indi.nonoas.xbh.common.error.MyUIException;
import indi.nonoas.xbh.view.toast.CoverableToast;

/**
 * @author Nonoas
 * @date 2021/11/14
 */
public class MyDrawableLayout extends RelativeLayout {

    private static final String TAG = MyDrawableLayout.class.getName();

    private boolean isNavExpanded = true;

    private View rootView;
    /**
     * 左侧菜单
     */
    private View navView;
    /**
     * 主布局
     */
    private View contentView;

    public MyDrawableLayout(Context context) {
        super(context);
    }

    public MyDrawableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyDrawableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            try {
                throw new MyUIException("MyDrawableLayout需满足有且仅有2个子View");
            } catch (MyUIException e) {
                e.printStackTrace();
            }
        }
        rootView = getRootView();
        navView = getChildAt(0);
        contentView = getChildAt(1);
    }

    private float downX = 0;
    private float currX = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                currX = ev.getX();
                move();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    private void move() {
        rootView.layout(0, rootView.getTop(), 500, rootView.getBottom());
    }
}
