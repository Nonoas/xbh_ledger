package indi.nonoas.xbh.utils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import indi.nonoas.xbh.common.error.ErrorEnum;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {
    private HttpUtil(){
    }

    /**
     * 检查errorCode是否匹配
     *
     * @param errorEnum 错误信息枚举
     * @param errStr    错误码
     * @return true：相同
     */
    public static boolean checkErrorCode(ErrorEnum errorEnum, String errStr) {
        return errorEnum.getErrorCode().equals(errStr);
    }

    /**
     * 从 Response 中解析出 body 到 json
     *
     * @param response 响应体
     * @return JSONObject
     */
    @NonNull
    public static JSONObject getRespBodyJson(@NonNull Response response) throws IOException {
        ResponseBody body = response.body();
        if (null == body) {
            return new JSONObject();
        }
        return  JSONObject.parseObject(body.string());
    }
}
