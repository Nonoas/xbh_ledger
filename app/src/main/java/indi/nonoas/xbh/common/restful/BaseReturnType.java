package indi.nonoas.xbh.common.restful;

/**
 * 返回类型基类
 *
 * @author : Nonoas
 * @time : 2021-04-20 9:42
 */
public class BaseReturnType implements IReturnType {

    /**
     * 错误代码，默认为成功 “00000”
     */
    private String errorCode = "00000";
    /**
     * 错误信息
     */
    private String errorMsg = "请求成功";


    public BaseReturnType() {
    }

    public BaseReturnType(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BaseReturnType(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
