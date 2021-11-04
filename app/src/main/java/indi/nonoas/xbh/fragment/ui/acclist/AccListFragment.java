package indi.nonoas.xbh.fragment.ui.acclist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.activity.AccAddActivity;
import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.databinding.FrgAccListBinding;
import indi.nonoas.xbh.fragment.ui.home.HomeViewModel;
import indi.nonoas.xbh.http.AccBalanceApi;
import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.pojo.Account;
import indi.nonoas.xbh.utils.DateTimeUtil;
import indi.nonoas.xbh.view.toast.CoverableToast;

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

    /**
     * 上下文菜单：修改
     */
    private final int CMENU_ID_MODIFY = 1;
    /**
     * 上下文菜单：删除
     */
    private final int CMENU_ID_DELETE = 2;

    public AccListFragment() {
    }


    public static AccListFragment newInstance() {
        AccListFragment fragment = new AccListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ==========================================   生命周期 beg  ===================================================

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
        binding = FrgAccListBinding.inflate(inflater, container, false);

        mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        mHomeViewModel.getBalanceData()
                .observe(getViewLifecycleOwner(), str -> binding.tvBalance.setText(str));

        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onStart() {
        super.onStart();
        lvAcc = binding.lvAcc;
        lvAcc.setEmptyView(binding.tvEmpty);
        // 为账户列表注册上下文菜单
        registerForContextMenu(lvAcc);
        MyAdapter adapter = new MyAdapter(getContext(), mBalanceList);
        adapter.clear();
        lvAcc.setAdapter(adapter);
        lvAcc.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            menu.add(0, CMENU_ID_MODIFY, 0, "修改");
            menu.add(0, CMENU_ID_DELETE, 1, "删除");
        });

        mHomeViewModel.getBalanceListData()
                .observe(getViewLifecycleOwner(), accBalances -> adapter.notifyDataSetChanged());

        this.initAccList();
        this.initBalance();

        binding.fab.setOnClickListener(view -> {
            AccItemPopWindow pw = new AccItemPopWindow(getContext());
            pw.setOnSelectedListener((v, item) -> {
                // 跳转至 账户添加设置 界面
                Intent intent = new Intent(requireActivity(), AccAddActivity.class);
                System.out.println(item.get(AccItemPopWindow.K_IMG));
                intent.putExtra(AccItemPopWindow.K_IMG, (Integer) item.get(AccItemPopWindow.K_IMG));
                intent.putExtra(AccItemPopWindow.K_NAME, (String) item.get(AccItemPopWindow.K_NAME));
                intentLauncher.launch(intent);
                pw.dismiss();
            });
            pw.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
        });


    }
    // ========================================== 生命周期 end ===================================================

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        CoverableToast.showToast(getContext(), info.position + "", Toast.LENGTH_SHORT);
        switch (item.getItemId()) {
            // 修改
            case CMENU_ID_MODIFY:
                break;
            // 删除
            case CMENU_ID_DELETE:
                mBalanceList.remove(info.position);
                mHomeViewModel.setBalanceList(mBalanceList);
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 设置数据并添加数据
     *
     * @param result 从其他activity接收的数据
     */
    private void setData(ActivityResult result) {
        Intent data = result.getData();
        if (null == data) {
            return;
        }
        Account account = new Account();
        // TODO 添加生成acc_id的方法
        account.setId(System.currentTimeMillis());
        account.setIconId(data.getIntExtra(AccItemPopWindow.K_IMG, R.drawable.ic_other_acc));
        account.setAccName(data.getStringExtra(AccItemPopWindow.K_NAME));
        account.setInitBalance(data.getStringExtra("balance"));

        addAcc(account);

    }

    private final Handler qryHandler = new Handler(msg -> {
        switch (msg.what) {
            case AccBalanceApi.QRY_SUCCESS:
                JSONObject json = (JSONObject) msg.obj;
                mBalanceList.clear();
                mBalanceList.addAll(JSONObject.parseArray(json.getString("data"), AccBalance.class));
                mHomeViewModel.setBalanceList(mBalanceList);
                Log.d(ILogTag.DEV, "查询账户列表成功:" + json.toJSONString());
                break;
            case AccBalanceApi.REQUEST_FAIL:
                CoverableToast.showShortToast(getContext(), "服务器请求异常", CoverableToast.FAIL);
                break;
        }
        return false;
    });

    /**
     * 从数据库查询账户余额
     */
    private void initAccList() {
        AccBalanceApi.qryBalance(AppStore.getUser().getUserId(), DateTimeUtil.getCurrDate(), qryHandler);
    }

    private void initBalance() {
        BigDecimal decimal = new BigDecimal(0);
        for (AccBalance acc : mBalanceList) {
            decimal = decimal.add(new BigDecimal(acc.getBalance()));
        }
        mHomeViewModel.setBalance(decimal.toString());
    }

    /**
     * 处添加账户api处理器
     */
    private final Handler addAccHandler = new Handler(msg -> {
        switch (msg.what) {
            case AccBalanceApi.ADD_SUCCESS:
                CoverableToast.showToast(getContext(), "添加账户成功", Toast.LENGTH_SHORT);
                initAccList();
                break;
            case AccBalanceApi.ADD_FAIL:
                JSONObject json = (JSONObject) msg.obj;
                CoverableToast.showToast(
                        getContext(),
                        String.format("%s,%s", json.getString("errorCode"), json.getString("errorMsg")),
                        Toast.LENGTH_SHORT
                );
                break;
            case AccBalanceApi.REQUEST_FAIL:
                CoverableToast.showToast(getContext(), "添加失败，访问服务器异常", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * 添加账户到数据库
     *
     * @param account 账户
     */
    private void addAcc(Account account) {

        // 添加到余额记录
        AccBalance accBalance = new AccBalance();
        accBalance.setIconId(account.getIconId());
        accBalance.setBalance(account.getInitBalance());
        accBalance.setUserId(AppStore.getUser().getUserId());
        accBalance.setAccName(account.getAccName());
        accBalance.setAccNo(account.getId());
        accBalance.setDate((long) DateTimeUtil.getCurrDate());
        // 调用 api
        AccBalanceApi.addAccBalance(accBalance, addAccHandler);

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
            return mList.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (null == view) {
                view = LayoutInflater.from(context).inflate(R.layout.item_acc, null);
                holder = new ViewHolder();
                holder.mImageView = view.findViewById(R.id.iv_acc_icon);
                holder.tvAccName = view.findViewById(R.id.tv_acc_name);
                holder.mEditText = view.findViewById(R.id.tv_balance);
                view.setTag(holder);
            } else {
                //复用holder
                holder = (ViewHolder) view.getTag();
            }
            AccBalance account = mList.get(i);
            int iconId = account.getIconId();
            if (iconId != 0) {
                holder.mImageView.setImageDrawable(AppCompatResources.getDrawable(context, iconId));
            } else {
                holder.mImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_other_acc));
            }
            holder.tvAccName.setText(account.getAccName());
            holder.mEditText.setText(account.getBalance());
            return view;
        }
    }

    static class ViewHolder {
        ImageView mImageView;
        TextView tvAccName;
        TextView mEditText;
    }
}