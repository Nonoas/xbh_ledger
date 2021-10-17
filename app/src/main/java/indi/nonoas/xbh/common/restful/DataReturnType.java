package indi.nonoas.xbh.common.restful;

/**
 * 单条数据回复类
 *
 * @author : Nonoas
 * @time : 2021-04-20 13:35
 */
public class DataReturnType extends BaseReturnType {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 生成 DataReturnType 对象
     *
     * @param data data数据
     * @return DataReturnType
     */
    public static DataReturnType create(Object data) {
        DataReturnType rtnType = new DataReturnType();
        rtnType.setData(data);
        return rtnType;
    }
}
