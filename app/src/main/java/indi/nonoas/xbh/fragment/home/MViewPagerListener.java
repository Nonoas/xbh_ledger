package indi.nonoas.xbh.fragment.home;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.utils.SystemUtil;

/**
 * @author Nonoas
 * @date 2021-09-20 20:43
 */
class MViewPagerListener {

	static class OnPageChangeListener implements ViewPager.OnPageChangeListener {

		private int currPos;

		private  AppBarLayout barLayout;
		private Toolbar toolbar;
		private final Fragment frg;

		OnPageChangeListener(Fragment frg) {
			this.frg = frg;
//			barLayout = this.frg.findViewById(R.id.appbar);
//			toolbar = this.frg.findViewById(R.id.toolbar);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			SystemUtil.toggleStatusBarColor(frg.requireActivity(), SystemUtil.StatusBarType.DARK);
		}

		@SuppressLint("ResourceType")
		@Override
		public void onPageSelected(int position) {
			this.currPos = position;
			Animation aniT, aniA;
			AnimationSet aniSet = new AnimationSet(true);
			aniSet.setDuration(300);
			if (0 == position) {
				toolbar.setTitle(R.string.menu_stats);
				aniT = new TranslateAnimation(0, 0, -barLayout.getHeight(), 0);
				aniA = new AlphaAnimation(0, 1);
				aniSet.addAnimation(aniT);
				aniSet.addAnimation(aniA);
				barLayout.setAnimation(aniSet);
				aniSet.start();
				barLayout.setVisibility(View.VISIBLE);
			} else {
				aniT = new TranslateAnimation(0, 0, 0, -barLayout.getHeight());
				aniA = new AlphaAnimation(1, 0);
				aniSet.addAnimation(aniT);
				aniSet.addAnimation(aniA);
				barLayout.setAnimation(aniSet);
				aniSet.start();
				barLayout.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state != ViewPager.SCROLL_STATE_IDLE) return;
			if (0 == currPos) {
//				SystemUtil.toggleStatusBarColor(view, SystemUtil.StatusBarType.DARK);
			} else {
//				SystemUtil.toggleStatusBarColor(view, SystemUtil.StatusBarType.LIGHT);
			}
		}
	}


}