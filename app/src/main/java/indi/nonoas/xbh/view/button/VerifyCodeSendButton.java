package indi.nonoas.xbh.view.button;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import indi.nonoas.xbh.utils.StringUtils;

public class VerifyCodeSendButton extends AppCompatButton {

    private final CountDownTimer timer;

    public VerifyCodeSendButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setEnabled(false);
                setText(StringUtils.format("已发送(%d)", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                setText("获取验证码");
            }
        };
    }

    /**
     * 倒计时
     */
    public void countdown() {
        timer.start();
    }
}