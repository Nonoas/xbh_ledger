package indi.nonoas.xbh.fragment.ui.acclist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.greenrobot.greendao.database.Database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FrgAccListBinding;
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

	private AccListViewModel mViewModel;

	private FrgAccListBinding binding;
	private final List<AccBalance> mBalanceList = new ArrayList<>();
	private ListView lvAcc;

	public AccListFragment() {
	}


	public static AccListFragment newInstance() {
		AccListFragment fragment = new AccListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	// ==========================================   生命周期 ===================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("ResourceType")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		mViewModel = new ViewModelProvider(this).get(AccListViewModel.class);
		mViewModel.getBalanceData()
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
			View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_acc, null);
			EditText etName = contentView.findViewById(R.id.et_name);
			EditText etBalance = contentView.findViewById(R.id.et_balance);
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setView(contentView)
					.setPositiveButton("添加",
							(dialog, which) -> {
								if ("".equals(etName.getText().toString())) {
									Toast.makeText(getContext(), "添加失败，账户名称为空", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								String sBalance = etBalance.getText().toString();
								Account account = new Account();

								account.setAccName(etName.getText().toString());
								account.setInitBalance(DefaultValueUtil.getValOrZero(sBalance));
								AccListFragment.this.addAcc(account);

								Toast.makeText(getContext(),
										String.format("添加%s\n余额：%s", etName.getText(), etBalance.getText()),
										Toast.LENGTH_SHORT).show();
							});
			builder.setNegativeButton("取消", null);
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
			alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(getString(R.color.soft_red)));
			alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(getString(R.color.soft_green1)));
			AccListFragment.this.notifyAccListChanged();
		});

		lvAcc.setOnItemLongClickListener((parent, view, position, id) -> {
			Toast.makeText(getContext(), "点击了" + id, Toast.LENGTH_SHORT).show();
			return false;
		});
	}

	/**
	 * 从数据库查询账户余额
	 */
	private void initAccList() {
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

		BaseAdapter adapter = new MyAdapter(getContext(), mBalanceList);
		lvAcc.setAdapter(adapter);
	}

	private void initBalance() {
		BigDecimal decimal = new BigDecimal(0);
		for (AccBalance acc : mBalanceList) {
			decimal = decimal.add(new BigDecimal(acc.getBalance()));
		}
		mViewModel.setBalance(decimal.toString());
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
		this.notifyAccListChanged();
		// 添加到余额记录
		AccBalance accBalance = new AccBalance();
		accBalance.setBalance(account.getInitBalance());
		accBalance.setAccName(account.getAccName());
		accBalance.setAccId(account.getId());
		accBalance.setTimestamp(System.currentTimeMillis());

		mBalanceList.add(accBalance);
		// 插入到余额记录表
		AccBalanceDao accBalanceDao = session.getAccBalanceDao();
		accBalanceDao.insert(accBalance);

		session.clear();

		// 更新余额
		BigDecimal oldBalance = new BigDecimal(mViewModel.getBalance());
		BigDecimal newBalance = oldBalance.add(new BigDecimal(account.getInitBalance()));
		mViewModel.setBalance(newBalance.toString());

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

		MyAdapter(Context context, List<AccBalance> list) {
			this.context = context;
			this.mList = list;
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
				holder.tvAccName = view.findViewById(R.id.tv_acc_name);
				holder.mEditText = view.findViewById(R.id.et_balance);
				view.setTag(holder);
			} else {
				//复用holder
				holder = (ViewHolder) view.getTag();
			}
			AccBalance account = mList.get(i);
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