package indi.nonoas.xbh.common.error;

/**
 * 系统错误枚举
 *
 * @author : Nonoas
 * @time : 2021-04-20 9:59
 */
public enum ErrorEnum implements IErrorReturnType {

    /**
     * 00000-请求成功
     */
    SUCCESS("00000", "请求成功"),

    /**
     * 未知错误，系统中未指定类型的错误
     */
    UNKNOWN_ERROR("00001", "未知错误"),

    /**
     * A0111-用户名已存在
     */
    USERID_EXISTED("A0111", "用户名已存在"),

    /**
     * A0132-邮箱验证码输入错误
     */
    EMAIL_VERIFICATION_CODE_ERROR("A0132", "邮箱验证码输入错误"),

    /**
     * A0200-登录失败，用户名或密码错误
     */
    WRONG_LOGIN_INFO("A0200", "登录失败，用户名或密码错误"),

    /**
     * A0230-用户未登录或登录已失效
     */
    LOGIN_EXPIRED("A0230", "用户未登录或登录已失效"),

    BAD_PARAM_VALUE("A0402", "请求参数值超出允许的范围");

    private final String errorCode;
    private final String errorMsg;

    ErrorEnum(String code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
