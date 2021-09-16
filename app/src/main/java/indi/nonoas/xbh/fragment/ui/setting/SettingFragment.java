package indi.nonoas.xbh.fragment.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        CollapsingToolbarLayout collapsingToolbarLayout = requireActivity().findViewById(R.id.collapsing_bar);
        collapsingToolbarLayout.setTitle(getString(R.string.menu_setting));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.actionbar_text_expand);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.actionbar_text_collapse);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}