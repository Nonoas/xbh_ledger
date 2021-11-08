package indi.nonoas.xbh.fragment.setting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);

        AppBarLayout actionBar = requireActivity()
                .findViewById(R.id.app_bar_main)
                .findViewById(R.id.appbar);
        actionBar.setBackgroundColor(Color.parseColor(getString(R.color.soft_green)));
        actionBar.setVisibility(View.VISIBLE);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}