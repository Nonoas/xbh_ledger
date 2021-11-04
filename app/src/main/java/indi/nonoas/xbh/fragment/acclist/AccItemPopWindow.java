package indi.nonoas.xbh.fragment.acclist;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.DialogAddAccBinding;

/**
 * 底部弹窗，用于 账户选择
 *
 * @author Nonoas
 * @date 2021-09-25 12:56
 */
public class AccItemPopWindow extends PopupWindow {

	public static final String K_IMG = "img";
	public static final String K_NAME = "name";

	private static boolean isShow = false;

	private final DialogAddAccBinding mBinding;

	private final Context mContext;

	private OnSelectedListener mOnSelectedListener;

	@SuppressWarnings("all")
	public AccItemPopWindow(Context context) {
		this.mContext = context;
		mBinding = DialogAddAccBinding.inflate(LayoutInflater.from(context));
		View contentView = mBinding.getRoot();
		setContentView(contentView);

		SimpleAdapter adapter = new SimpleAdapter(
				context,
				getData(),
				R.layout.item_acc_select,
				new String[]{K_IMG, K_NAME},
				new int[]{R.id.iv_acc_icon, R.id.tv_acc_name}
		);
		mBinding.lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		mBinding.lv.setOnItemClickListener((parent, view, position, id) -> {
			Map item = (Map) adapter.getItem(position);
			mOnSelectedListener.onSelected(view, item);
		});

		mBinding.vEmpty.setOnClickListener(e -> this.dismiss());

		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		setOutsideTouchable(true);
	}

	/**
	 * 设置 listview 的数据
	 */
	@NonNull
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>(4);

		map.put(K_IMG, R.drawable.ic_alipay);
		map.put(K_NAME, "支付宝");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_weixin);
		map.put(K_NAME, "微信");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_dfcf);
		map.put(K_NAME, "东方财富");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_zgyh);
		map.put(K_NAME, "中国银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_yzyh);
		map.put(K_NAME, "邮政银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_zsyh);
		map.put(K_NAME, "招商银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_jiangsuyh);
		map.put(K_NAME, "江苏银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_jsyh);
		map.put(K_NAME, "建设银行");
		list.add(map);

		map = new HashMap<>(4);
		map.put(K_IMG, R.drawable.ic_other_acc);
		map.put(K_NAME, "其他账户");
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
		Animation animT = new TranslateAnimation(0, 0, dm.heightPixels, 0);
		Animation animA = new AlphaAnimation(0, 1);
		animT.setDuration(350);
		animA.setDuration(350);
		mBinding.getRoot().startAnimation(animA);
		mBinding.llContent.startAnimation(animT);
		mBinding.getRoot().setVisibility(View.VISIBLE);

	}


	@Override
	public void dismiss() {

		if (!isShow) {
			return;
		}
		isShow = false;

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		Animation animT = new TranslateAnimation(0, 0, 0, dm.heightPixels);
		Animation animA = new AlphaAnimation(1, 0);
		animT.setDuration(350);
		animA.setDuration(350);

		animA.setAnimationListener(new Animation.AnimationListener() {
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

		mBinding.llContent.startAnimation(animT);
		mBinding.getRoot().startAnimation(animA);
		mBinding.getRoot().setVisibility(View.INVISIBLE);

	}

	public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
		mOnSelectedListener = onSelectedListener;
	}

	/**
	 * 选中监听，提供外部调用
	 */
	public interface OnSelectedListener {

		/**
		 * 选中时调用的方法
		 *
		 * @param view 被点击的view
		 * @param item 选中的item数据
		 */
		void onSelected(View view, Map<String, Object> item);
	}
}
