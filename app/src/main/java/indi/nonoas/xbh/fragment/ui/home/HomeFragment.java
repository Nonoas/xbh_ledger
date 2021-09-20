package indi.nonoas.xbh.fragment.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentHomeBinding;
import indi.nonoas.xbh.fragment.ui.acclist.AccListFragment;
import indi.nonoas.xbh.fragment.ui.stats.StatsFragment;
import indi.nonoas.xbh.utils.SystemUtil;


public class HomeFragment extends Fragment {

	private HomeViewModel homeViewModel;
	private FragmentHomeBinding binding;

	public HomeFragment() {

	}

	public static HomeFragment newInstance(String param1, String param2) {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();

		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

		binding = FragmentHomeBinding.inflate(inflater, container, false);

		ViewPager vp = binding.vpHome;

		vp.setAdapter(new MyFragmentStatePagerAdapter(getChildFragmentManager()));
		vp.addOnPageChangeListener(new MViewPagerListener.OnPageChangeListener(requireActivity()));
		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}


	/**
	 * ViewPager适配器
	 */
	@SuppressWarnings("all")
	private static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

		StatsFragment statsFragment = StatsFragment.newInstance();
		AccListFragment accListFragment = AccListFragment.newInstance();

		public MyFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
		}

		public MyFragmentStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
			super(fm, behavior);
		}

		@NonNull
		@Override
		public Fragment getItem(int position) {
			if (0 == position)
				return statsFragment;
			else
				return accListFragment;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}


}