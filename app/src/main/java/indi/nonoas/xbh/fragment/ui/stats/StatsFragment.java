package indi.nonoas.xbh.fragment.ui.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentStatsBinding;


public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    private FragmentStatsBinding binding;

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);

        CollapsingToolbarLayout collapsingToolbarLayout = requireActivity().findViewById(R.id.collapsing_bar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.actionbar_text_expand);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.actionbar_text_collapse);

        binding = FragmentStatsBinding.inflate(inflater, container, false);

        BarChart barChart = binding.barChart;

        List<BarEntry> list = new ArrayList<>();
        list.add(new BarEntry(1, 3));
        list.add(new BarEntry(2, 8));
        list.add(new BarEntry(3, 6));
        list.add(new BarEntry(4, 9));

        List<BarEntry> list2 = new ArrayList<>();
        list.add(new BarEntry(1, 7));
        list.add(new BarEntry(2, 4));
        list.add(new BarEntry(3, 6));
        list.add(new BarEntry(4, 1));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMaximum(25);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rAxis = barChart.getAxisRight();
        rAxis.setEnabled(false);
        rAxis.setDrawGridLines(false);

        YAxis lAxis = barChart.getAxisLeft();
        lAxis.setDrawGridLines(false);

        BarData data = new BarData();


        BarDataSet math = new BarDataSet(list2, "数学");
        math.setColor(Color.rgb(0, 255, 0));
        data.addDataSet(math);


        BarDataSet chinese = new BarDataSet(list, "语文");
        chinese.setColor(Color.rgb(255, 0, 0));
        data.addDataSet(chinese);

        List<Integer> cols = new ArrayList<>();
        cols.add(Color.BLACK);
        cols.add(Color.BLUE);
        data.setValueTextColors(cols);

        data.setBarWidth(0.5f);
        data.groupBars(0, 1f, 0);

        barChart.setData(data);


        barChart.invalidate();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}