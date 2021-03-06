package indi.nonoas.xbh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.ActivityAccAddBinding;
import indi.nonoas.xbh.fragment.acclist.AccItemEnum;
import indi.nonoas.xbh.utils.StringUtils;
import indi.nonoas.xbh.utils.SystemUtil;

public class AccAddActivity extends AppCompatActivity {

    private ActivityAccAddBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAccAddBinding.inflate(getLayoutInflater());

        // 切换状态栏样式
        SystemUtil.toggleStatusBarColor(this, SystemUtil.StatusBarType.GREEN);

        setContentView(mBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 接收 Intent 数据
        Intent intent = getIntent();
        String id = intent.getStringExtra(AccItemEnum.K_ID);
        String typeName = intent.getStringExtra(AccItemEnum.K_NAME);
        int iconId = intent.getIntExtra(AccItemEnum.K_IMG, R.drawable.ic_other_acc);

        // 设置默认值
        // 账户名称
        mBinding.etAccName.setText(typeName);
        //账户余额
        mBinding.etAccBalance.setText("0");

        // 设置UI
        mBinding.tvAccType.setText(typeName);
        mBinding.ivAccIcon.setImageDrawable(AppCompatResources.getDrawable(this, iconId));

        mBinding.btnSave.setOnClickListener(view -> {
            String accName = mBinding.etAccName.getText().toString().trim();
            if (StringUtils.isEmpty(accName)) {
                Toast.makeText(this, "账户名称不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String accBalance = mBinding.etAccBalance.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra(AccItemEnum.K_ID, id);
            resultIntent.putExtra(AccItemEnum.K_IMG, iconId);
            resultIntent.putExtra(AccItemEnum.K_NAME, accName);
            resultIntent.putExtra("balance", accBalance);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}