package indi.nonoas.xbh.common.restful;

import java.util.List;

/**
 * 查询通用返回类
 *
 * @author : Nonoas
 * @time : 2021-04-19 16:42
 */
public class QryReturnType extends BaseReturnType {

    private List<?> rows;
    private long totalCount;

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
