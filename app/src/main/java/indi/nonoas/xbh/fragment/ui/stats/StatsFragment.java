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
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.AppBarLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.common.ColorTemplate;
import indi.nonoas.xbh.databinding.FragmentStatsBinding;
import indi.nonoas.xbh.view.FlexibleScrollView;


public class StatsFragment extends Fragment {

	private StatsViewModel statsViewModel;
	private FragmentStatsBinding binding;

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

		binding = FragmentStatsBinding.inflate(inflater, container, false);

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
		BarChart barChart = binding.barChart;

		barChart.setDescription(null);

		List<BarEntry> list = new ArrayList<>();
		list.add(new BarEntry(1, 3));
		list.add(new BarEntry(2, 8));
		list.add(new BarEntry(3, 6));
		list.add(new BarEntry(4, 9));

		XAxis xAxis = barChart.getXAxis();
		xAxis.setAxisMaximum(7);
		xAxis.setDrawGridLines(false);
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

		YAxis rAxis = barChart.getAxisRight();
		rAxis.setEnabled(false);
		rAxis.setDrawGridLines(false);

		YAxis lAxis = barChart.getAxisLeft();
		lAxis.setDrawGridLines(false);

		BarData data = new BarData();

		BarDataSet dataSet = new BarDataSet(list, "每日余额");
		dataSet.setColor(Color.parseColor(getString(R.color.soft_red)));
		data.addDataSet(dataSet);

		data.setBarWidth(0.3f);
		barChart.setData(data);

		barChart.invalidate();
	}

	/**
	 * 生成饼状图
	 */
	private void genPieChart() {

		PieChart chart = binding.pieChart;
		chart.setUsePercentValues(true);
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
		pieEntries.add(new PieEntry(0.2f, "东方财富"));
		pieEntries.add(new PieEntry(0.2f, "天天基金"));
		pieEntries.add(new PieEntry(0.3f, "招商银行"));
		pieEntries.add(new PieEntry(0.3f, "支付宝"));

		PieDataSet set = new PieDataSet(pieEntries, null);
		set.setColors(ColorTemplate.PIE_CHART_COLORS);
		set.setSliceSpace(1.6f);
		set.setSelectionShift(8f);

		pieData.setDataSet(set);
		pieData.setValueTextSize(10f);
		pieData.setValueTextColor(Color.WHITE);
		pieData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler)
				-> DecimalFormat.getInstance().format(value) + "%");

		chart.setData(pieData);
		chart.setEntryLabelTextSize(10f);

		chart.invalidate();
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

}