package indi.nonoas.xbh.common.error;

/**
 * @author : Nonoas
 * @time : 2021-04-20 11:15
 */
public class BusinessException extends Exception {


    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;


    public BusinessException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(IErrorReturnType returnType) {
        super(returnType.getErrorMsg());
        this.errorCode = returnType.getErrorCode();
        this.errorMsg = returnType.getErrorMsg();
    }

    public BusinessException(IErrorReturnType returnType, String errorMsg) {
        super(errorMsg);
        this.errorCode = returnType.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
