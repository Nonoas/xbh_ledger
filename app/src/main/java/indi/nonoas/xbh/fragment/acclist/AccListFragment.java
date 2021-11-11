package indi.nonoas.xbh.fragment.acclist;

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
import java.util.Map;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.activity.AccAddActivity;
import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.databinding.FrgAccListBinding;
import indi.nonoas.xbh.fragment.home.HomeViewModel;
import indi.nonoas.xbh.http.AccBalanceApi;
import indi.nonoas.xbh.http.HttpUICallback;
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

    private HomeViewModel homeModel;

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

        // 接收跳转后的结果
        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Activity.RESULT_OK == result.getResultCode()) {
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

        homeModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

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

        // 余额列表数据观察
        homeModel.getBalanceListData().observe(
                getViewLifecycleOwner(),
                accBalances -> {
                    adapter.notifyDataSetChanged();
                    refreshBalance();
                }
        );
        // 总余额数据观察
        homeModel.getBalanceData().observe(
                getViewLifecycleOwner(),
                balance -> binding.tvBalance.setText(balance)
        );

        this.initAccList();

        binding.fab.setOnClickListener(view -> {
            AccItemPopWindow pw = new AccItemPopWindow(getContext());
            pw.setOnSelectedListener((v, item) -> {
                // 跳转至 账户添加设置 界面
                Intent intent = new Intent(requireActivity(), AccAddActivity.class);
                intent.putExtra(AccItemEnum.K_ID, (String) item.get(AccItemEnum.K_ID));
                intent.putExtra(AccItemEnum.K_IMG, (Integer) item.get(AccItemEnum.K_IMG));
                intent.putExtra(AccItemEnum.K_NAME, (String) item.get(AccItemEnum.K_NAME));
                intentLauncher.launch(intent);
                pw.dismiss();
            });
            pw.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁所有消息，避免内存泄露
        delAccHandler.removeCallbacks(null);
        qryHandler.removeCallbacks(null);
        addAccHandler.removeCallbacks(null);
    }

    // ========================================== 生命周期 end ===================================================

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            // 修改
            case CMENU_ID_MODIFY:
                break;
            // 删除
            case CMENU_ID_DELETE:
                if (null == AppStore.getUser()) {
                    break;
                }
                AccBalanceApi.delAccBalance(
                        AppStore.getUser().getUserId(),
                        mBalanceList.get(index).getAccNo(),
                        delAccHandler);
                mBalanceList.remove(index);
                homeModel.setBalanceList(mBalanceList);
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private final Handler delAccHandler = new Handler(new HttpUICallback() {
        @Override
        protected void onMsgSuccess(JSONObject json) {
            CoverableToast.showSuccessToast(getContext(), "删除成功");
        }

        @Override
        protected void onMsgError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), "删除失败，" + json.getString("errorMsg"));
        }

        @Override
        protected void handleError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), "删除失败，服务器异常");
        }
    });

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
        addAcc(data);
    }

    private final Handler qryHandler = new Handler(new HttpUICallback() {
        @Override
        protected void onMsgSuccess(JSONObject json) {
            mBalanceList.clear();
            mBalanceList.addAll(JSONObject.parseArray(json.getString("data"), AccBalance.class));
            homeModel.setBalanceList(mBalanceList);
            Log.d(ILogTag.DEV, "查询账户列表成功:" + json.toJSONString());
        }

        @Override
        protected void onMsgError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(),json.getString("errorMsg"));
        }

        @Override
        protected void handleError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), "服务器请求异常");
        }
    });

    /**
     * 从数据库查询账户余额
     */
    private void initAccList() {
        AccBalanceApi.qryBalance(AppStore.getUser().getUserId(), DateTimeUtil.getCurrDate(), qryHandler);
    }

    private void refreshBalance() {
        BigDecimal decimal = new BigDecimal(0);
        for (AccBalance acc : mBalanceList) {
            decimal = decimal.add(new BigDecimal(acc.getBalance()));
        }
        homeModel.setBalance(decimal.toString());
    }

    /**
     * 处添加账户api处理器
     */
    private final Handler addAccHandler = new Handler(new HttpUICallback() {
        @Override
        protected void onMsgSuccess(JSONObject json) {
            CoverableToast.showToast(getContext(), "添加账户成功", Toast.LENGTH_SHORT);
            initAccList();
        }

        @Override
        protected void onMsgError(int msgWhat, JSONObject json) {
            CoverableToast.showToast(
                    getContext(),
                    String.format("%s,%s", json.getString("errorCode"), json.getString("errorMsg")),
                    Toast.LENGTH_SHORT
            );
        }

        @Override
        protected void handleError(int msgWhat, JSONObject json) {
            CoverableToast.showFailureToast(getContext(), "添加失败，访问服务器异常");
        }
    });

    /**
     * 添加账户到数据库
     *
     * @param data 账户余额信息
     */
    private void addAcc(Intent data) {
        Map<String, Integer> icMap = AccItemEnum.ICONID_MAP;
        String accNo = data.getStringExtra(AccItemEnum.K_ID);
        Integer integer = icMap.get(accNo);
        int icId = null == integer ? 0 : integer;

        AccBalance balance = new AccBalance();
        balance.setAccNo(accNo);
        balance.setIconId(icId);
        balance.setUserId(AppStore.getUser().getUserId());
        balance.setAccName(data.getStringExtra(AccItemEnum.K_NAME));
        balance.setBalance(data.getStringExtra("balance"));
        balance.setDate(DateTimeUtil.getCurrDate());

        // 调用 api
        AccBalanceApi.addAccBalance(balance, addAccHandler);

    }

    /**
     * listView 适配器
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
            Integer iconId = AccItemEnum.ICONID_MAP.get(account.getAccNo());
            if (null == iconId || 0 == iconId) {
                holder.mImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_other_acc));
            } else {
                holder.mImageView.setImageDrawable(AppCompatResources.getDrawable(context, iconId));
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