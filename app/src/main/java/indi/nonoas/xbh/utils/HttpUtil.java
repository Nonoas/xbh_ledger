package indi.nonoas.xbh.utils;

import indi.nonoas.xbh.common.error.ErrorEnum;

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
}
