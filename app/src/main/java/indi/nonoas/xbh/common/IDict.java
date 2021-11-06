package indi.nonoas.xbh.common;

/**
 * 字典常量类
 */
public interface IDict {
    /**
     * 周期单位
     */
    interface Period {
        int YEAR = 0;
        int MONTH = 1;
        int DAY = 2;
    }

    interface LockStatus {
        int OFFLINE = 0;
        int LOGIN = 1;
        int FREEZE = 2;
    }
}