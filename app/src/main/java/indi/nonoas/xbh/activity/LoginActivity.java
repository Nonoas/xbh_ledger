package indi.nonoas.xbh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import indi.nonoas.xbh.MainActivity;
import indi.nonoas.xbh.databinding.ActivityLoginBinding;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.UserDao;
import indi.nonoas.xbh.http.LoginInfoApi;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.GreenDaoUtil;
import indi.nonoas.xbh.utils.SystemUtil;
import indi.nonoas.xbh.view.toast.CoverableToast;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private User user;

    /**
     * 处理登录的匿名类对象
     */
    private final Handler handler = new Handler(msg -> {
        JSONObject json;
        switch (msg.what) {
            case LoginInfoApi.REQUEST_FAIL:
                changeLoginBtnStatus(true);
                CoverableToast.showShortToast(LoginActivity.this, "登录失败，请求数据失败", CoverableToast.FAIL);
                break;
            // 密码错误
            case LoginInfoApi.WRONG_LOGIN_INFO:
                changeLoginBtnStatus(true);
                json = (JSONObject) msg.obj;
                CoverableToast.showToast(LoginActivity.this, json.getString("errorMsg"), CoverableToast.FAIL, Toast.LENGTH_LONG);
                break;
            // 登陆成功
            case LoginInfoApi.LOGIN_SUCCESS:
                addUser();
                json = (JSONObject) msg.obj;
                CoverableToast.showToast(LoginActivity.this, json.getString("errorMsg"), CoverableToast.SUCCESS, Toast.LENGTH_LONG);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SystemUtil.toggleStatusBarColor(this, SystemUtil.StatusBarType.LIGHT);

        // 按钮监听 beg
        // 登录按钮
        binding.btnLogin.setOnClickListener(v -> {
            String userId = binding.etName.getText().toString();
            String password = binding.etPassword.getText().toString();
            user = new User(userId, "测试用户", password);
            changeLoginBtnStatus(false);
            LoginInfoApi.login(handler, user);
        });
        // 注册按钮
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistryActivity.class));
        });
        // 按钮监听 end

    }

    /**
     * 新建用户
     */
    private void addUser() {
        DaoSession daoSession = GreenDaoUtil.getDaoSession(this);
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }


    private void changeLoginBtnStatus(boolean enable) {
        if (enable) {
            binding.etName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            binding.etPassword.setInputType(129);
            binding.btnLogin.setText("登录");
        } else {
            binding.etName.setInputType(InputType.TYPE_NULL);
            binding.etPassword.setInputType(InputType.TYPE_NULL);
            binding.btnLogin.setText("登录中...");
        }
        binding.btnLogin.setEnabled(enable);
    }
}