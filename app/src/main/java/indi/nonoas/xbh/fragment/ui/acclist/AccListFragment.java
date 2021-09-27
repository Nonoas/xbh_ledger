package indi.nonoas.xbh.fragment.ui.acclist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.greenrobot.greendao.database.Database;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.activity.AccAddActivity;
import indi.nonoas.xbh.databinding.FrgAccListBinding;
import indi.nonoas.xbh.fragment.ui.home.HomeViewModel;
import indi.nonoas.xbh.greendao.AccBalanceDao;
import indi.nonoas.xbh.greendao.AccountDao;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.pojo.Account;
import indi.nonoas.xbh.utils.DefaultValueUtil;
import indi.nonoas.xbh.utils.GreenDaoUtil;

/**
 * 账户列表: {@link Fragment} 的子类
 * Use the {@link AccListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccListFragment extends Fragment {

	private HomeViewModel mHomeViewModel;

	private FrgAccListBinding binding;
	private final List<AccBalance> mBalanceList = new ArrayList<>();
	private ListView lvAcc;

	private ActivityResultLauncher<Intent> intentLauncher;

	public AccListFragment() {
	}


	public static AccListFragment newInstance() {
		AccListFragment fragment = new AccListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	// ==========================================   生命周期  ===================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		intentLauncher = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(),
				result -> {
					if (result.getResultCode() == Activity.RESULT_OK) {
						setData(result);
					}
				}
		);

	}

	@SuppressLint("ResourceType")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
		mHomeViewModel.getBalanceData()
				.observe(getViewLifecycleOwner(), str -> binding.tvBalance.setText(str));

		binding = FrgAccListBinding.inflate(inflater, container, false);

		return binding.getRoot();
	}

	@SuppressLint("ResourceType")
	@Override
	public void onStart() {
		super.onStart();
		lvAcc = binding.lvAcc;
		this.initAccList();
		this.initBalance();

		binding.fab.setOnClickListener(view -> {
			AccItemPopWindow pw = new AccItemPopWindow(getContext());
			pw.setOnSelectedListener((v, item) -> {
				String typeName = (String) item.get(AccItemPopWindow.K_NAME);
				Toast.makeText(getContext(), typeName + "被点击了", Toast.LENGTH_SHORT).show();

				// 跳转至 账户添加设置 界面
				Intent intent = new Intent(requireActivity(), AccAddActivity.class);
				intent.putExtra(AccItemPopWindow.K_IMG, (Integer) item.get(AccItemPopWindow.K_IMG));
				intent.putExtra(AccItemPopWindow.K_NAME, (String) item.get(AccItemPopWindow.K_NAME));

				intentLauncher.launch(intent);


				pw.dismiss();
			});
			pw.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
		});

		lvAcc.setOnItemLongClickListener((parent, view, position, id) -> {
			Toast.makeText(getContext(), "点击了" + id, Toast.LENGTH_SHORT).show();
			return false;
		});
	}

	/**
	 * 设置数据
	 *
	 * @param result 从其他activity接收的数据
	 */
	private void setData(ActivityResult result) {
		Intent data = result.getData();
		if (null == data) {
			return;
		}
		Account account = new Account();
		account.setAccName(data.getStringExtra(AccItemPopWindow.K_NAME));
		account.setInitBalance(data.getStringExtra("balance"));

		addAcc(account);

	}

	/**
	 * 从数据库查询账户余额
	 */
	private void initAccList() {
		MyAdapter adapter = new MyAdapter(getContext(), mBalanceList);
		adapter.clear();

		DaoSession session = GreenDaoUtil.getDaoSession(getContext());
		Database database = session.getDatabase();
		Cursor cursor = database.rawQuery("select *,max(_id) from acc_balance group by acc_id", null);
		AccBalance balance;
		while (cursor.moveToNext()) {
			balance = new AccBalance();
			balance.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			balance.setAccId(cursor.getLong(cursor.getColumnIndex("ACC_ID")));
			balance.setAccName(cursor.getString(cursor.getColumnIndex("ACC_NAME")));
			balance.setBalance(cursor.getString(cursor.getColumnIndex("BALANCE")));
			balance.setTimestamp(cursor.getLong(cursor.getColumnIndex("TIMESTAMP")));
			mBalanceList.add(balance);
		}

		lvAcc.setAdapter(adapter);

		mHomeViewModel.setBalanceList(mBalanceList);
		mHomeViewModel.getBalanceListData()
				.observe(getViewLifecycleOwner(), accBalances -> adapter.notifyDataSetChanged());
	}

	private void initBalance() {
		BigDecimal decimal = new BigDecimal(0);
		for (AccBalance acc : mBalanceList) {
			decimal = decimal.add(new BigDecimal(acc.getBalance()));
		}
		mHomeViewModel.setBalance(decimal.toString());
	}

	/**
	 * 添加账户到数据库
	 *
	 * @param account 账户
	 */
	private void addAcc(Account account) {
		DaoSession session = GreenDaoUtil.getDaoSession(getContext());
		AccountDao accDao = session.getAccountDao();
		accDao.insert(account);

		// 添加到余额记录
		AccBalance accBalance = new AccBalance();
		accBalance.setBalance(account.getInitBalance());
		accBalance.setAccName(account.getAccName());
		accBalance.setAccId(account.getId());
		accBalance.setTimestamp(System.currentTimeMillis());

		mBalanceList.add(accBalance);
		mHomeViewModel.setBalanceList(mBalanceList);
		// 插入到余额记录表
		AccBalanceDao accBalanceDao = session.getAccBalanceDao();
		accBalanceDao.insert(accBalance);

		session.clear();

		// 更新余额
		BigDecimal oldBalance = new BigDecimal(mHomeViewModel.getBalance());
		BigDecimal newBalance = oldBalance.add(new BigDecimal(account.getInitBalance()));
		mHomeViewModel.setBalance(newBalance.toString());

	}

	private void notifyAccListChanged() {
		BaseAdapter adapter = (BaseAdapter) lvAcc.getAdapter();
		adapter.notifyDataSetChanged();
	}

	/**
	 * listView 适配器
	 * // todo 可改用 RecycleView
	 */
	static class MyAdapter extends BaseAdapter {

		private final List<AccBalance> mList;
		private final Context context;
		private final int[] icons = {R.drawable.ic_alipay,
				R.drawable.ic_dfcf, R.drawable.ic_zgyh,
				R.drawable.ic_zsyh, R.drawable.ic_yzyh};

		MyAdapter(Context context, List<AccBalance> list) {
			this.context = context;
			this.mList = list;
		}

		public void clear() {
			mList.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int i) {
			return mList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return mList.get(i).getId();
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if (null == view) {
				view = LayoutInflater.from(context).inflate(R.layout.item_acc, null);
				holder = new ViewHolder();
				holder.mImageView = view.findViewById(R.id.iv_acc_icon);
				holder.tvAccName = view.findViewById(R.id.tv_acc_name);
				holder.mEditText = view.findViewById(R.id.et_balance);
				view.setTag(holder);
			} else {
				//复用holder
				holder = (ViewHolder) view.getTag();
			}
			AccBalance account = mList.get(i);
			holder.mImageView.setImageDrawable(AppCompatResources.getDrawable(context, icons[(int) (Math.random() * 4 + 1)]));
			holder.tvAccName.setText(account.getAccName());
			holder.mEditText.setText(account.getBalance());
			return view;
		}
	}

	static class ViewHolder {
		ImageView mImageView;
		TextView tvAccName;
		EditText mEditText;
	}
}