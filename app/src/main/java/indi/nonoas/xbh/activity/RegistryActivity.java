package indi.nonoas.xbh.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import indi.nonoas.xbh.databinding.ActivityRegistryBinding;
import indi.nonoas.xbh.http.LoginInfoApi;
import indi.nonoas.xbh.utils.StringUtils;
import indi.nonoas.xbh.utils.SystemUtil;
import indi.nonoas.xbh.view.toast.CoverableToast;
import indi.nonoas.xbh.view.button.VerifyCodeSendButton;
import indi.nonoas.xbh.view.toast.ToastType;

public class RegistryActivity extends AppCompatActivity {

    private ActivityRegistryBinding binding;

    private EditText emailPrefix;
    private Spinner emailSuffix;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private VerifyCodeSendButton btnSendVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityRegistryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SystemUtil.toggleStatusBarColor(this, SystemUtil.StatusBarType.LIGHT);

        // 赋值UI变量 beg
        emailPrefix = binding.emailPrefix;
        emailSuffix = binding.emailSuffix;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        btnSendVerifyCode = binding.btnSendVerifyCode;
        // 赋值UI变量 end

        // 监听beg
        // 发送验证码
        binding.btnSendVerifyCode.setOnClickListener(view -> sendVerifyCode());
        // 注册按钮
        binding.btnRegister.setOnClickListener(view -> registry());
        // 监听end

    }

    /**
     * 注册返回api
     *
     * @see LoginInfoApi#registry
     */
    private final Handler registryHandler = new Handler(msg -> {
        switch (msg.what) {
            // 注册成功
            case LoginInfoApi.REGISTRY_SUCCESS:
                CoverableToast.showSuccessToast(this, "注册成功");
                this.finish();
                break;
            case LoginInfoApi.REGISTRY_FAILURE:
                JSONObject json = (JSONObject) msg.obj;
                CoverableToast.showShortToast(this, "注册失败，" + json.getString("errorMsg"), CoverableToast.FAIL);
                break;
            case LoginInfoApi.REQUEST_FAIL:
                CoverableToast.showShortToast(this, "注册失败，服务器异常", ToastType.FAIL);
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * 注册，调用注册api
     */
    private void registry() {
        if (!checkSubmit()) {
            return;
        }
        String userId = emailPrefix.getText().toString() + emailSuffix.getSelectedItem().toString();
        String password = etPassword.getText().toString();
        String verifyCode = binding.etVerifyCode.getText().toString();
        LoginInfoApi.registry(userId, password, verifyCode, registryHandler);
    }

    /**
     * 发送验证码
     */
    private void sendVerifyCode() {
        if (checkFrom()) {
            String email = emailPrefix.getText().toString() + emailSuffix.getSelectedItem().toString();
            LoginInfoApi.sendVerityCode(email, sendVerifyCodeHandler);
            btnSendVerifyCode.setText("正在获取");
        }
    }

    /**
     * 处理验证码发送api的返回信息
     *
     * @see LoginInfoApi#sendVerityCode
     */
    private final Handler sendVerifyCodeHandler = new Handler(msg -> {
        switch (msg.what) {
            case LoginInfoApi.SEND_VERIFY_CODE_SUCCESS:
                btnSendVerifyCode.countdown();
                break;
            case LoginInfoApi.SEND_VERIFY_CODE_FAIL:
                JSONObject json = (JSONObject) msg.obj;
                btnSendVerifyCode.setText("获取验证码");
                CoverableToast.showShortToast(this, json.getString("errorMsg"), CoverableToast.FAIL);
                break;
            case LoginInfoApi.REQUEST_FAIL:
                CoverableToast.showShortToast(this, "服务器异常，获取验证码失败", CoverableToast.FAIL);
                btnSendVerifyCode.setText("获取验证码");
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * 校验是否能提交注册
     *
     * @return true：通过校验
     */
    private boolean checkSubmit() {
        return checkFrom() && checkVerifyCode();
    }

    /**
     * 表单校验
     *
     * @return true：通过校验
     */
    private boolean checkFrom() {
        // 邮箱校验
        if (StringUtils.isEmpty(emailPrefix.getText().toString())) {
            CoverableToast.showToast(this, "邮箱不能为空！", 0);
            return false;
        }
        // 校验密码
        String psw = etPassword.getText().toString();
        String confirmPsw = etConfirmPassword.getText().toString();
        if (StringUtils.isEmpty(psw)) {
            CoverableToast.showToast(this, "密码不能为空！", 0);
            return false;
        }
        if (!psw.equals(confirmPsw)) {
            CoverableToast.showToast(this, "两次输入的密码不一致！", 0);
            return false;
        }
        return true;
    }

    /**
     * 验证码非空校验
     *
     * @return true：通过校验
     */
    private boolean checkVerifyCode() {
        // 验证码非空校验
        if (StringUtils.isEmpty(binding.etVerifyCode.getText().toString())) {
            CoverableToast.showToast(this, "请输入验证码！", 0);
            return false;
        }
        return true;
    }
}