package indi.nonoas.xbh.view;

/**
 * 需要联网的，声明了各种网络请求状下的处理方法
 *
 * @author Nonoas
 * @date 2021/11/13
 */
interface INetworked {

    /**
     * 正在请求网络
     */
    void onRequest();

    /**
     * 网络请求失败
     */
    void onRequestFailed();

    /**
     * 网络请求成功
     */
    void onRequestSuccess();

}
