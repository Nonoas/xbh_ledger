package indi.nonoas.xbh.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.WindowInsetsControllerCompat;

import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.databinding.ActivityAccAddBinding;
import indi.nonoas.xbh.fragment.ui.acclist.AccItemPopWindow;
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

		Intent intent = getIntent();
		String typeName = intent.getStringExtra(AccItemPopWindow.K_NAME);
		int iconId = intent.getIntExtra(AccItemPopWindow.K_IMG, 0);

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
			resultIntent.putExtra(AccItemPopWindow.K_NAME, accName);
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