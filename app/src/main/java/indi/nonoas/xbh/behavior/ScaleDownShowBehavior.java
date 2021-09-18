package indi.nonoas.xbh.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import indi.nonoas.xbh.utils.AnimatorUtil;

// FAB 行为控制器
public class ScaleDownShowBehavior extends FloatingActionButton.Behavior {

    public ScaleDownShowBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {

        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild,
                target, axes, ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    private boolean isAnimateIng = false;   // 是否正在动画
    private boolean isShow = true;  // 是否已经显示

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimateIng && isShow) { // 手指上滑，隐藏FAB
            AnimatorUtil.scaleHide(child, new StateListener() {
                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    isShow = false;
                }
            });
        } else if ((dyConsumed < 0 || dyUnconsumed < 0 && !isAnimateIng && !isShow)) {
            AnimatorUtil.scaleShow(child, new StateListener() {
                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    isShow = true;
                }
            });// 手指下滑，显示FAB
        }
    }

    class StateListener implements ViewPropertyAnimatorListener {
        @Override
        public void onAnimationStart(View view) {
            isAnimateIng = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            isAnimateIng = false;
        }

        @Override
        public void onAnimationCancel(View view) {
            isAnimateIng = false;
        }
    }
}