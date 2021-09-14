package indi.nonoas.xbh.fragment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentHomeBinding;
import indi.nonoas.xbh.fragment.ui.acclist.AccListFragment;
import indi.nonoas.xbh.fragment.ui.stats.StatsFragment;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    /**
     * 外层可折叠的actionbar
     */
    private CollapsingToolbarLayout ctBar;

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

        ctBar = requireActivity().findViewById(R.id.collapsing_bar);
        ctBar.setExpandedTitleTextAppearance(R.style.actionbar_text_expand);
        ctBar.setCollapsedTitleTextAppearance(R.style.actionbar_text_collapse);
        ctBar.setTitle(getString(R.string.menu_stats));
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        ViewPager vp = binding.vpHome;

        vp.setAdapter(new MyFragmentStatePagerAdapter(getChildFragmentManager()));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (1 == position) {
                    ctBar.setTitle(getString(R.string.menu_acc));
                    return;
                }
                ctBar.setTitle(getString(R.string.menu_stats));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    private static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        StatsFragment statsFragment = StatsFragment.newInstance();
        AccListFragment accListFragment = AccListFragment.newInstance(null, null);

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