package indi.nonoas.xbh.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnimatorUtil {

	private static final LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

	private static final AccelerateInterpolator LINER_INTERPOLATOR = new AccelerateInterpolator();


	// 显示view
	public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
		view.setVisibility(View.VISIBLE);
		ViewCompat.animate(view)
				.scaleX(1.0f)
				.scaleY(1.0f)
				.alpha(1.0f)
				.setDuration(200)
				.setListener(viewPropertyAnimatorListener)
				.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
				.start();
	}

	// 隐藏view
	public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
		ViewCompat.animate(view)
				.scaleX(0.0f)
				.scaleY(0.0f)
				.alpha(0.0f)
				.setDuration(200)
				.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
				.setListener(viewPropertyAnimatorListener)
				.start();
	}

	// 显示view
	public static void translateShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
		view.setVisibility(View.VISIBLE);
		ViewCompat.animate(view)
				.translationY(0)
				.setDuration(400)
				.setListener(viewPropertyAnimatorListener)
				.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
				.start();
	}

	// 隐藏view
	public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
		view.setVisibility(View.VISIBLE);
		ViewCompat.animate(view)
				.translationY(260)
				.setDuration(400)
				.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
				.setListener(viewPropertyAnimatorListener)
				.start();
	}
	
}