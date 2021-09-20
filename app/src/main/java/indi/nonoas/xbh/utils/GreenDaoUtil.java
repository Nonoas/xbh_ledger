package indi.nonoas.xbh.utils;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import indi.nonoas.xbh.common.IDBSetting;
import indi.nonoas.xbh.greendao.DaoMaster;
import indi.nonoas.xbh.greendao.DaoSession;

public class GreenDaoUtil {

	/**
	 * 获取一个数据库session
	 *
	 * @param context 上下文对象
	 * @return DaoSession
	 */
	public static DaoSession getDaoSession(Context context) {
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, IDBSetting.Name.XBH_DB);
		Database db = helper.getWritableDb();
		return new DaoMaster(db).newSession();
	}

}
