package indi.nonoas.xbh;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import indi.nonoas.xbh.common.log.ILogTag;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://9h825s.natappfree.cc/login/login?userId=1&password=2")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject json = (JSONObject) JSONObject.parse(response.body().string());
                System.out.println(json);
            }else {
                System.out.println("登录失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}