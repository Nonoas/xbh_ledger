package indi.nonoas.xbh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import indi.nonoas.xbh.MainActivity;
import indi.nonoas.xbh.databinding.ActivitySignUpBinding;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.UserDao;
import indi.nonoas.xbh.http.LoginInfoApi;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.GreenDaoUtil;
import indi.nonoas.xbh.view.CoverableToast;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private User user;

    /**
     * 处理登录的匿名类对象
     */
    private final Handler handler = new Handler(msg -> {
        JSONObject json;
        switch (msg.what) {
            case LoginInfoApi.REQUEST_FAIL:
                changeLoginBtnStatus(true);
                CoverableToast.showToast(SignUpActivity.this, "登录失败，请求数据失败！", Toast.LENGTH_LONG);
                break;
            // 密码错误
            case LoginInfoApi.WRONG_LOGIN_INFO:
                changeLoginBtnStatus(true);
                json = (JSONObject) msg.obj;
                CoverableToast.showToast(SignUpActivity.this, json.getString("errorMsg"), Toast.LENGTH_LONG);
                break;
            // 登陆成功
            case LoginInfoApi.LOGIN_SUCCESS:
                addUser();
                json = (JSONObject) msg.obj;
                CoverableToast.showToast(SignUpActivity.this, json.getString("errorMsg"), Toast.LENGTH_LONG);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
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
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(v -> {
            String userId = binding.etName.getText().toString();
            String password = binding.etPassword.getText().toString();
            user = new User(userId, "测试用户", password);
            changeLoginBtnStatus(false);
            LoginInfoApi.login(handler, user);
        });

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
            binding.btnAdd.setText("登录");
        } else {
            binding.etName.setInputType(InputType.TYPE_NULL);
            binding.etPassword.setInputType(InputType.TYPE_NULL);
            binding.btnAdd.setText("登录中...");
        }
        binding.btnAdd.setEnabled(enable);
    }
}