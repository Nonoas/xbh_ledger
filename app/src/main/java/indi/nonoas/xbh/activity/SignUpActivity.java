package indi.nonoas.xbh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import indi.nonoas.xbh.MainActivity;
import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.ActivitySignUpBinding;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.UserDao;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.GreenDaoUtil;

public class SignUpActivity extends AppCompatActivity {

	private ActivitySignUpBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		binding = ActivitySignUpBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.btnAdd.setOnClickListener(v -> {
			addUser();
			startActivity(new Intent(SignUpActivity.this, MainActivity.class));
		});

	}

	/**
	 * 新建用户
	 */
	private void addUser() {
		EditText tvName = binding.tvName;
		DaoSession daoSession = GreenDaoUtil.getDaoSession(this);
		UserDao userDao = daoSession.getUserDao();
		User user = new User();
		user.setName(tvName.getText().toString());
		userDao.insert(user);
	}
}