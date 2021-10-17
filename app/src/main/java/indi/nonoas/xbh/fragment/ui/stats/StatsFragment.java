package indi.nonoas.xbh.fragment.ui.stats;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.common.ColorTemplate;
import indi.nonoas.xbh.databinding.FrgStatsBinding;
import indi.nonoas.xbh.fragment.ui.home.HomeViewModel;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.utils.GreenDaoUtil;
import indi.nonoas.xbh.utils.TimeUtil;
import indi.nonoas.xbh.view.FlexibleScrollView;
import indi.nonoas.xbh.view.chart.DateValueFormatter;


public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    private HomeViewModel mHomeViewModel;
    private FrgStatsBinding binding;
    private final List<AccBalance> mBalanceList = new ArrayList<>();

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


        this.genBarChart();
        this.genPieChart();

        return binding.getRoot();
    }

    /**
     * 生成柱状图
     */
    @SuppressLint("ResourceType")
    private void genBarChart() {

        LineChart lineChart = binding.barChartContainer.barChart;

        lineChart.setDescription(null);
        lineChart.setScaleEnabled(false);

        long startTimeStamp = TimeUtil.add(System.currentTimeMillis(), TimeUtil.TimeMilliEnum.DAY, -6);

        List<Entry> list = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            list.add(new Entry(i, (float) Math.random() * 100));
        }

        XAxis xAxis = lineChart.getXAxis();

        xAxis.setGranularity(1f);

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateValueFormatter("MM/dd", startTimeStamp));

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

        DaoSession session = GreenDaoUtil.getDaoSession(getContext());
        Database database = session.getDatabase();
        Cursor cursor = database.rawQuery("select *,max(_id) from acc_balance group by acc_id", null);

        AccBalance balance;
        while (cursor.moveToNext()) {
            balance = new AccBalance();
            balance.setSerialNo(cursor.getLong(cursor.getColumnIndex("_id")));
            balance.setAccNo(cursor.getLong(cursor.getColumnIndex("ACC_ID")));
            balance.setAccName(cursor.getString(cursor.getColumnIndex("ACC_NAME")));
            balance.setBalance(cursor.getString(cursor.getColumnIndex("BALANCE")));
            balance.setDate(cursor.getLong(cursor.getColumnIndex("TIMESTAMP")));
            mBalanceList.add(balance);
        }

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
        for (AccBalance b : mBalanceList) {
            PieEntry entry = new PieEntry(Float.parseFloat(b.getBalance()), b.getAccName());
            pieEntries.add(entry);
        }

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