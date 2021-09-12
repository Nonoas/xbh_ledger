package indi.nonoas.xbh.fragment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
        ctBar.setTitle(getString(R.string.menu_home));
        ctBar.setExpandedTitleTextAppearance(R.style.actionbar_text_expand);
        ctBar.setCollapsedTitleTextAppearance(R.style.actionbar_text_collapse);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ViewPager2 vp2 = binding.vp2Home;

        vp2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    ctBar.setTitle(getString(R.string.menu_stats));
                    return StatsFragment.newInstance();
                }
                ctBar.setTitle(getString(R.string.menu_acc));
                return AccListFragment.newInstance(null, null);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    ctBar.setTitle(getString(R.string.menu_stats));
                    return;
                }
                ctBar.setTitle(getString(R.string.menu_acc));
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}