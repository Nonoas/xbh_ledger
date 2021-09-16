package indi.nonoas.xbh.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class MyNestedScrollChild extends LinearLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper mScrollingChildHelper;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mLastTouchY;
    private int mVisiableHeight;
    private int mFullHeight;
    private int mCanScrollY;

    public MyNestedScrollChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScrollingChildHelper = new NestedScrollingChildHelper(this);
        mScrollingChildHelper.setNestedScrollingEnabled(true);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = (int) (e.getRawY() + 0.5f);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (e.getRawY() + 0.5f);
                int dy = mLastTouchY - y;
                mLastTouchY = y;
                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1];
                }
                scrollBy(0, dy);
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y > mCanScrollY) {
            y = mCanScrollY;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mVisiableHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mVisiableHeight = getMeasuredHeight();
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (mFullHeight <= 0) {
                mFullHeight = getMeasuredHeight();
                mCanScrollY = mFullHeight - mVisiableHeight;
            }
        }
    }
}
