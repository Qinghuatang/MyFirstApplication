package com.jnu.student.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库总操作类
 */
public class DBMaster {

    private static final String DB_NAME = "PlayTask.db";
    private static final int DB_VERSION = 1;

    /** 上下文 */
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBOpenHelper mDbOpenHelper;

    /** 数据表操作类实例化 */
    public TaskDBDao mTaskDBDao;
    public AwardDBDao mAwardDBDao;
    public CountDBDao mCountDBDao;

    public DBMaster(Context context){
        mContext = context;
        mTaskDBDao = new TaskDBDao(mContext);
        mAwardDBDao = new AwardDBDao(mContext);
        mCountDBDao = new CountDBDao(mContext);
    }

    /**
     * 打开数据库
     */
    public void openDataBase() {
        mDbOpenHelper = new DBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
        // 设置数据库的SQLiteDatabase
        mTaskDBDao.setDatabase(mDatabase);
        mAwardDBDao.setDatabase(mDatabase);
        mCountDBDao.setDatabase(mDatabase);
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    /** 创建该数据库下task表的语句 */
    private static final String mTaskSqlStr = "CREATE TABLE IF NOT EXISTS " + TaskDBDao.TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "taskType TEXT CHECK(taskType IN ('Daily', 'Weekly', 'Common')) NOT NULL, " +
            "time DATETIME NOT NULL," +
            "content VARCHAR NOT NULL," +
            "point INTEGER NOT NULL," +
            "finishedNum INTEGER," +
            "num INTEGER," +
            "classification VARCHAR);";

    /** 创建该数据库下award表的语句 */
    private static final String mAwardSqlStr = "CREATE TABLE IF NOT EXISTS " + AwardDBDao.TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "time DATETIME NOT NULL," +
            "content VARCHAR NOT NULL," +
            "point INTEGER NOT NULL," +
            "buyNum INTEGER," +
            "classification VARCHAR NOT NULL);";

    /** 创建该数据库下count表的语句 */
    private static final String mCountSqlStr = "CREATE TABLE IF NOT EXISTS " + CountDBDao.TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "date DATE NOT NULL DEFAULT(CURRENT_DATE)," +
            "point INTEGER NOT NULL," +
            "content VARCHAR NOT NULL," +
            "classification VARCHAR NOT NULL);";



    /** 删除该数据库下task表的语句 */
    private static final String mTaskDelSql = "DROP TABLE IF EXISTS " + TaskDBDao.TABLE_NAME;

    /** 删除该数据库下award表的语句 */
    private static final String mAwardDelSql = "DROP TABLE IF EXISTS " + AwardDBDao.TABLE_NAME;

    /** 删除该数据库下count表的语句 */
    private static final String mCountDelSql = "DROP TABLE IF EXISTS " + CountDBDao.TABLE_NAME;


    /**
     * 数据表打开帮助类
     */
    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mTaskSqlStr);
            db.execSQL(mAwardSqlStr);
            db.execSQL(mCountSqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(mTaskDelSql);
            db.execSQL(mAwardDelSql);
            db.execSQL(mCountDelSql);
            onCreate(db);
        }
    }
}

