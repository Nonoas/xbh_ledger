package indi.nonoas.xbh.fragment.ui.stats;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.common.ColorTemplate;
import indi.nonoas.xbh.databinding.FrgStatsBinding;
import indi.nonoas.xbh.fragment.ui.home.HomeViewModel;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.utils.DateTimeUtil;
import indi.nonoas.xbh.view.FlexibleScrollView;
import indi.nonoas.xbh.view.chart.DateValueFormatter;


public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    private HomeViewModel mHomeViewModel;
    private FrgStatsBinding binding;
    private final List<AccBalance> mBalanceList = new ArrayList<>();
    private final List<AccBalance> periodBalanceList = new ArrayList<>();

    private int snackBarFlag = 10;

    Toast toast;

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FrgStatsBinding.inflate(inflater, container, false);

        FlexibleScrollView fsv = binding.fsv;
        ImageView ivHeader = binding.statsHeaderImg;

        AppBarLayout actionBar = requireActivity()
                .findViewById(R.id.app_bar_main)
                .findViewById(R.id.appbar);

        fsv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int height = ivHeader.getHeight() - actionBar.getHeight();
            if (scrollY > height) {
                actionBar.setBackgroundColor(Color.parseColor(getString(R.color.soft_green)));
            } else {
                actionBar.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        binding.fab.setOnClickListener(view -> {
            if (toast != null) {
                toast.cancel();
            }
            if (snackBarFlag > 0) {
                toast = Toast.makeText(getContext(), "再点" + snackBarFlag + "次可触发事件", Toast.LENGTH_SHORT);
                snackBarFlag--;
            } else {
                toast = Toast.makeText(getContext(), "行啦行啦！别点了！还没开发呢！", Toast.LENGTH_SHORT);
            }
            toast.show();
        });


        this.genLineChart();
        this.genPieChart();

        initData();

        return binding.getRoot();
    }

    /**
     * 初始化数据
     */
    private void initData(){

    }

    /**
     * 生成折线图
     */
    @SuppressLint("ResourceType")
    private void genLineChart() {

        LineChart lineChart = binding.barChartContainer.barChart;

        lineChart.setDescription(null);
        lineChart.setScaleEnabled(false);

        List<Entry> list = new ArrayList<>();

        statsViewModel.setBalanceList(periodBalanceList);
        statsViewModel.getBalanceListData().observe(getViewLifecycleOwner(), accBalances -> {
            list.clear();
            for (AccBalance balance : accBalances) {
                list.add(new Entry(balance.getDate(), Float.parseFloat(balance.getBalance())));
            }
            lineChart.notifyDataSetChanged();
            lineChart.postInvalidate();
        });

        XAxis xAxis = lineChart.getXAxis();

        xAxis.setGranularity(1f);

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rAxis = lineChart.getAxisRight();
        rAxis.setEnabled(false);
        rAxis.setDrawGridLines(false);

        YAxis lAxis = lineChart.getAxisLeft();
        lAxis.setEnabled(false);
        lAxis.setDrawGridLines(false);

        LineData data = new LineData();

        LineDataSet dataSet = new LineDataSet(list, "每日余额");

        dataSet.setColor(Color.parseColor(getString(R.color.soft_red)));
        dataSet.setDrawCircles(false);

        data.addDataSet(dataSet);

        lineChart.setData(data);
        lineChart.setExtraBottomOffset(20f);
        lineChart.animateY(800, Easing.EaseInOutQuad);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    /**
     * 生成饼状图
     */
    private void genPieChart() {

        PieChart chart = binding.pieChart;
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawCenterText(true);
        chart.setCenterText("账户余额分布");
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(50f);
        chart.setTransparentCircleRadius(54f);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        PieData pieData = new PieData();
        List<PieEntry> pieEntries = new ArrayList<>();

        mHomeViewModel.setBalanceList(mBalanceList);
        mHomeViewModel.getBalanceListData().observe(getViewLifecycleOwner(), accBalances -> {
            pieEntries.clear();
            List<AccBalance> balanceList = mHomeViewModel.getBalanceList();
            for (AccBalance b : balanceList) {
                PieEntry entry = new PieEntry(Float.parseFloat(b.getBalance()), b.getAccName());
                pieEntries.add(entry);
            }
            chart.notifyDataSetChanged();
            chart.postInvalidate();
        });

        PieDataSet set = new PieDataSet(pieEntries, null);
        set.setColors(ColorTemplate.PIE_CHART_COLORS);
        set.setSliceSpace(1.6f);
        set.setSelectionShift(8f);

        pieData.setDataSet(set);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        // pieData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler)
        // 		-> DecimalFormat.getInstance().format(value) + "%");

        chart.setData(pieData);
        chart.setEntryLabelTextSize(10f);

        chart.animateY(800, Easing.EaseInOutQuad);

        chart.notifyDataSetChanged();
        chart.postInvalidate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}