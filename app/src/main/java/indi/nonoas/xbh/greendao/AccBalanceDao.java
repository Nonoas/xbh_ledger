package indi.nonoas.xbh.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import indi.nonoas.xbh.pojo.AccBalance;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACC_BALANCE".
*/
public class AccBalanceDao extends AbstractDao<AccBalance, Long> {

    public static final String TABLENAME = "ACC_BALANCE";

    /**
     * Properties of entity AccBalance.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AccId = new Property(1, Long.class, "accId", false, "ACC_ID");
        public final static Property Timestamp = new Property(2, Long.class, "timestamp", false, "TIMESTAMP");
        public final static Property AccName = new Property(3, String.class, "accName", false, "ACC_NAME");
        public final static Property Balance = new Property(4, String.class, "balance", false, "BALANCE");
    }


    public AccBalanceDao(DaoConfig config) {
        super(config);
    }
    
    public AccBalanceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACC_BALANCE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACC_ID\" INTEGER," + // 1: accId
                "\"TIMESTAMP\" INTEGER," + // 2: timestamp
                "\"ACC_NAME\" TEXT," + // 3: accName
                "\"BALANCE\" TEXT);"); // 4: balance
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ACC_BALANCE_ACC_ID_DESC_TIMESTAMP_DESC ON \"ACC_BALANCE\"" +
                " (\"ACC_ID\" DESC,\"TIMESTAMP\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACC_BALANCE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AccBalance entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long accId = entity.getAccId();
        if (accId != null) {
            stmt.bindLong(2, accId);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(3, timestamp);
        }
 
        String accName = entity.getAccName();
        if (accName != null) {
            stmt.bindString(4, accName);
        }
 
        String balance = entity.getBalance();
        if (balance != null) {
            stmt.bindString(5, balance);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AccBalance entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long accId = entity.getAccId();
        if (accId != null) {
            stmt.bindLong(2, accId);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(3, timestamp);
        }
 
        String accName = entity.getAccName();
        if (accName != null) {
            stmt.bindString(4, accName);
        }
 
        String balance = entity.getBalance();
        if (balance != null) {
            stmt.bindString(5, balance);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AccBalance readEntity(Cursor cursor, int offset) {
        AccBalance entity = new AccBalance( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // accId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // timestamp
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // accName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // balance
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AccBalance entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTimestamp(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setAccName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBalance(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AccBalance entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AccBalance entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AccBalance entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}