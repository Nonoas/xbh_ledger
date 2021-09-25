package indi.nonoas.xbh.fragment.ui.acclist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.DialogAddAccBinding;
import indi.nonoas.xbh.pojo.AccItem;

/**
 * 底部弹窗，用于 账户选择
 *
 * @author Nonoas
 * @date 2021-09-25 12:56
 */
public class AccItemPopWindow extends PopupWindow {

	private static boolean isShow = false;

	private final DialogAddAccBinding mBinding;

	private final Context mContext;

	@SuppressLint("InflateParams")
	public AccItemPopWindow(Context context) {
		this.mContext = context;
		mBinding = DialogAddAccBinding.inflate(LayoutInflater.from(context));
		View contentView = mBinding.getRoot();
		setContentView(contentView);

		SimpleAdapter adapter = new SimpleAdapter(
				context,
				getData(),
				R.layout.item_acc_select,
				new String[]{"img", "name"},
				new int[]{R.id.iv_acc_icon, R.id.tv_acc_name}
		);
		mBinding.lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		mBinding.lv.setOnItemClickListener((parent, view, position, id) -> {
			TextView tv = view.findViewById(R.id.tv_acc_name);
			Toast.makeText(context, "你想添加" + tv.getText(), Toast.LENGTH_SHORT).show();
			this.dismiss();
		});

		mBinding.vEmpty.setOnClickListener(e -> {
			this.dismiss();
		});

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		setOutsideTouchable(true);
	}

	/**
	 * 设置 listview 的数据
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>(4);

		map.put("img", R.drawable.ic_alipay);
		map.put("name", "支付宝");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_weixin);
		map.put("name", "微信");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_dfcf);
		map.put("name", "东方财富");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_zgyh);
		map.put("name", "中国银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_yzyh);
		map.put("name", "邮政银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_zsyh);
		map.put("name", "招商银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_jiangsuyh);
		map.put("name", "江苏银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_jsyh);
		map.put("name", "建设银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put("img", R.drawable.ic_other_acc);
		map.put("name", "其他账户");
		list.add(map);

		return list;
	}


	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {

		if (isShow) {
			return;
		}
		isShow = true;

		super.showAtLocation(parent, gravity, x, y);

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		Animation anim = new TranslateAnimation(0, 0, dm.heightPixels, 0);
		Animation anim2 = new AlphaAnimation(0, 1);
		anim.setDuration(400);
		anim2.setDuration(400);
		mBinding.getRoot().startAnimation(anim2);
		mBinding.llContent.startAnimation(anim);
		mBinding.getRoot().setVisibility(View.VISIBLE);

	}


	@Override
	public void dismiss() {

		if (!isShow) {
			return;
		}
		isShow = false;

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		Animation anim = new TranslateAnimation(0, 0, 0, dm.heightPixels);
		Animation anim2 = new AlphaAnimation(1, 0);
		anim.setDuration(350);
		anim2.setDuration(350);

		anim2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 动画完成时让 PopupWindow 消失掉
				// 否则第二次无法唤起弹窗
				AccItemPopWindow.super.dismiss();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		mBinding.llContent.startAnimation(anim);
		mBinding.getRoot().startAnimation(anim2);
		mBinding.getRoot().setVisibility(View.INVISIBLE);

	}
}
