package indi.nonoas.xbh.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;


public class FlexibleScrollView extends ScrollView {

    private static final String TAG = "FlexibleScrollView";

    //移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动 100 * MOVE_FACTOR px
    private static final float MOVE_FACTOR = 0.4f;

    //松开手指后, 界面回到正常位置需要的动画时间
    private static final int ANIM_TIME = 100;

    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;

    //手指按下时的Y值, 用于在移动时计算移动距离
    //如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
    private float startY;

    //用于记录正常的布局位置
    private final Rect originalRect = new Rect();

    //手指按下时记录是否可以继续下拉
    private boolean canPullDown = false;

    //手指按下时记录是否可以继续上拉
    private boolean canPullUp = false;

    //在手指滑动的过程中记录是否移动了布局
    private boolean isMoved = false;

    private ProgressBar progressBar;

    public FlexibleScrollView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public FlexibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
            if (contentView instanceof LinearLayout) {
                LinearLayout ll = (LinearLayout) contentView;
                progressBar = new ProgressBar(getContext());
                ll.addView(progressBar, 0);
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
                layoutParams.height = 1;
                progressBar.setLayoutParams(layoutParams);
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (contentView == null) return;

        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView
                .getRight(), contentView.getBottom());
    }

    /**
     * 在触摸事件中, 处理上拉和下拉的逻辑
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断是否可以上拉和下拉
                canPullDown = canPullDown();
                canPullUp = canPullUp();
                //记录按下时的Y值
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoved) break;  //如果没有移动布局， 则跳过执行
                // 开启动画
                TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
                anim.setDuration(ANIM_TIME);
                contentView.startAnimation(anim);
                // 设置回到正常的布局位置
                contentView.layout(originalRect.left, originalRect.top,
                        originalRect.right, originalRect.bottom);
                //将标志位设回false
                canPullDown = canPullUp = isMoved = false;

                showProgressBar(false, 0);

                break;
            case MotionEvent.ACTION_MOVE:
                // 在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
                if (!canPullDown && !canPullUp) {
                    startY = ev.getY();
                    canPullDown = canPullDown();
                    canPullUp = canPullUp();
                    break;
                }
                // 计算手指移动的距离
                float nowY = ev.getY();
                int deltaY = (int) (nowY - startY);
                //是否应该移动布局
                boolean shouldMove =
                        (canPullDown && deltaY > 0)    //可以下拉， 并且手指向下移动
                                || (canPullUp && deltaY < 0)    //可以上拉， 并且手指向上移动
                                || (canPullUp && canPullDown); //既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）

                if (shouldMove) {
                    //计算偏移量
                    int offset = (int) (deltaY * MOVE_FACTOR);
                    //随着手指的移动而移动布局
                    contentView.layout(originalRect.left, originalRect.top + offset,
                            originalRect.right, originalRect.bottom + offset);

                    showProgressBar(true, offset);

                    isMoved = true;  //记录移动了布局
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 判断是否滚动到顶部
     */
    private boolean canPullDown() {
        return getScrollY() == 0 ||
                contentView.getHeight() < getHeight() + getScrollY();
    }

    /**
     * 判断是否滚动到底部
     */
    private boolean canPullUp() {
        return contentView.getHeight() <= getHeight() + getScrollY();
    }


    private void showProgressBar(boolean show, int offset) {
        if (originalRect.top + offset < 0 || null == progressBar) {
            return;
        }
        if (show) {
            progressBar.setVisibility(VISIBLE);
            ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
            layoutParams.height = originalRect.top + offset;
            int padding = (int) (layoutParams.height * 0.2);
            progressBar.setPadding(padding, padding, padding, padding);
            progressBar.setLayoutParams(layoutParams);
        } else {
            progressBar.setVisibility(GONE);
        }

    }


}