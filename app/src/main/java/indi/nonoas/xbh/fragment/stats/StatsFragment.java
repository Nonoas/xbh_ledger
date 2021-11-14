package indi.nonoas.xbh.fragment.stats;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
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
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.common.ColorTemplate;
import indi.nonoas.xbh.common.IDict;
import indi.nonoas.xbh.databinding.FrgStatsBinding;
import indi.nonoas.xbh.fragment.home.HomeViewModel;
import indi.nonoas.xbh.http.AccBalanceApi;
import indi.nonoas.xbh.http.HttpUICallback;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.LogUtil;
import indi.nonoas.xbh.view.FlexibleScrollView;
import indi.nonoas.xbh.view.chart.DateValueFormatter;
import indi.nonoas.xbh.view.toast.CoverableToast;


public class StatsFragment extends Fragment {

    private StatsViewModel statsModel;
    private HomeViewModel homeModel;
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

        statsModel = new ViewModelProvider(this).get(StatsViewModel.class);
        homeModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FrgStatsBinding.inflate(inflater, container, false);

        FlexibleScrollView fsv = binding.fsv;
        ImageView ivHeader = binding.statsHeaderImg;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        lineChartHandler.removeCallbacks(null);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        refreshLineChart();
    }

    /**
     * 刷新折线图数据
     */
    private void refreshLineChart() {
        User user = AppStore.getUser();
        if (null == user) {
            return;
        }
        // 如果数据为空才进行网络请求
        LogUtil.d(getClass().getName(), String.valueOf(statsModel.getTotBalanceList().isEmpty()));
        if (statsModel.getTotBalanceList().isEmpty()) {
            AccBalanceApi.qryPeriodTotBalance(user.getUserId(), IDict.Period.DAY, 7, lineChartHandler);
        }
    }

    private final Handler lineChartHandler = new Handler(new HttpUICallback() {
        @Override
        protected void onMsgSuccess(JSONObject json) {
            String data = json.getString("data");
            List<AccBalance> balances = JSONObject.parseArray(data, AccBalance.class);
            periodBalanceList.clear();
            periodBalanceList.addAll(balances);
            statsModel.setTotBalanceList(periodBalanceList);
        }

        @Override
        protected void onMsgError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), json.getString("errorMsg"));
        }

        @Override
        protected void handleError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), "服务器异常");
        }
    });

    /**
     * 生成折线图
     */
    @SuppressLint("ResourceType")
    private void genLineChart() {

        LineChart lineChart = binding.barChartContainer.barChart;

        lineChart.setDescription(null);
        lineChart.setScaleEnabled(false);

        List<Entry> entries = new ArrayList<>();

        // 数据绑定
        statsModel.setTotBalanceList(periodBalanceList);
        statsModel.getTotBalanceListData().observe(getViewLifecycleOwner(), accBalances -> {
            entries.clear();
            int i = 0;
            for (AccBalance balance : accBalances) {
                float totBalance = Float.parseFloat(balance.getTotBalance());
                entries.add(new Entry(i++, totBalance));
            }

            XAxis xAxis = lineChart.getXAxis();

            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new DateValueFormatter("MM-dd", periodBalanceList));
            xAxis.setGranularity(1f);

            YAxis rAxis = lineChart.getAxisRight();
            rAxis.setEnabled(false);
            rAxis.setDrawGridLines(false);

            YAxis lAxis = lineChart.getAxisLeft();
            lAxis.setEnabled(false);
            lAxis.setDrawGridLines(false);

            LineData data = new LineData();

            entries.sort(new EntryXComparator());
            LineDataSet dataSet = new LineDataSet(entries, "总余额");

            dataSet.setColor(Color.parseColor(getString(R.color.soft_red)));
            dataSet.setDrawCircles(false);

            data.addDataSet(dataSet);

            lineChart.setData(data);
            lineChart.setExtraBottomOffset(20f);
            lineChart.animateY(800, Easing.EaseInOutQuad);

            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
            lineChart.notifyDataSetChanged();
            lineChart.postInvalidate();
        });


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

        homeModel.setBalanceList(mBalanceList);
        homeModel.getBalanceListData().observe(getViewLifecycleOwner(), accBalances -> {
            pieEntries.clear();
            List<AccBalance> balanceList = homeModel.getBalanceList();
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