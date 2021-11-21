package indi.nonoas.xbh.view;

/**
 * 需要加载数据的的，声明了各种网络请求状下的处理方法
 *
 * @author Nonoas
 * @date 2021/11/13
 */
public interface IDataLoadView {

    /**
     * 加载时
     */
    void onLoad();

    /**
     * 加载失败时
     */
    void onLoadFailed();

    /**
     * 加载成功时
     */
    void onLoadSuccess();

}
