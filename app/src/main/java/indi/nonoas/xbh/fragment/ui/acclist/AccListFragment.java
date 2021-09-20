package indi.nonoas.xbh.fragment.ui.acclist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FrgAccListBinding;
import indi.nonoas.xbh.view.FlexibleScrollView;

/**
 * 账户列表: {@link Fragment} 的子类
 * Use the {@link AccListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccListFragment extends Fragment {

	FrgAccListBinding binding;
	int snackBarFlag = 10;
	private Toast toast;

	public AccListFragment() {
	}


	public static AccListFragment newInstance() {
		AccListFragment fragment = new AccListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("ResourceType")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		binding = FrgAccListBinding.inflate(inflater, container, false);

		ListView lvAcc = binding.lvAcc;
		lvAcc.setAdapter(new MyAdapter());

		FlexibleScrollView fsv = binding.fsv;

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

		lvAcc.setOnItemClickListener((parent, view, position, id) -> {
			Toast.makeText(getContext(), "点击了" + id, Toast.LENGTH_SHORT).show();
		});

		return binding.getRoot();
	}


	/**
	 * listView 适配器
	 * // todo 可改用 RecycleView
	 */
	class MyAdapter extends BaseAdapter {

		private List<String> accList;
		private Context context;

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int i) {
			return i;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if (null == view) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.item_acc, null);
				holder = new ViewHolder();
				holder.textView = view.findViewById(R.id.textView2);
				view.setTag(holder);
			} else {
				//复用holder
				holder = (ViewHolder) view.getTag();
			}
			return view;
		}
	}

	static class ViewHolder {
		TextView textView;
	}
}