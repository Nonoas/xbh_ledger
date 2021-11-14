package indi.nonoas.xbh.greendao;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * 实现了greendao 的 OpenHelper
 *
 * @author Nonoas
 * @date 2021/11/14
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // todo 升级数据库
    }
}
