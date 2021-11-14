package indi.nonoas.xbh.utils;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import indi.nonoas.xbh.common.IDBSetting;
import indi.nonoas.xbh.greendao.DaoMaster;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.MyOpenHelper;

public class GreenDaoUtil {

	/**
	 * 获取一个数据库session
	 *
	 * @param context 上下文对象
	 * @return DaoSession
	 */
	public static DaoSession getDaoSession(Context context) {
		MyOpenHelper helper = new MyOpenHelper(context, IDBSetting.Name.XBH_DB);
		Database db = helper.getWritableDb();
		return new DaoMaster(db).newSession();
	}

}
