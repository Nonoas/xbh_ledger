package indi.nonoas.xbh.http;

import indi.nonoas.xbh.common.error.ErrorEnum;

public class BaseApi {

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";
    private static final String BASE_ADDRESS = "fpna9d.natappfree.cc";

    public static String fullURL(String url) {
        return PROTOCOL_HTTP + "://" + BASE_ADDRESS + "/" + url;
    }

    /**
     * 检查errorCode是否匹配
     * @param errorEnum 错误信息枚举
     * @param errStr 错误码
     * @return true：相同
     */
    public static boolean checkErrorCode(ErrorEnum errorEnum, String errStr) {
        return errorEnum.getErrorCode().equals(errStr);
    }

}
