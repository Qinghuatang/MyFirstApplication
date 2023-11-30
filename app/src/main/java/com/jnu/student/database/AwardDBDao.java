package com.jnu.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jnu.student.data.Award;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AwardDBDao {

    /** 数据表名称 */
    public static final String TABLE_NAME = "award";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_TIME = "time";
    public static String KEY_CONTENT = "content";
    public static String KEY_POINT = "point";
    public static String KEY_BUY_NUM = "buyNum";
    public static String KEY_CLASSIFICATION = "classification";

    private SQLiteDatabase mDatabase;
    /** 上下文 */
    private Context mContext;
    /** 数据库打开帮助类 */
    private DBMaster.DBOpenHelper mDbOpenHelper;

    public AwardDBDao(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param award
     * @return
     */
    public long insertData(Award award) {
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(award.getTime()));
        values.put(KEY_CONTENT, award.getContent());
        values.put(KEY_POINT, award.getPoint());
        values.put(KEY_BUY_NUM, award.getBuyNum());
        values.put(KEY_CLASSIFICATION, award.getClassification());

        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     * @param id
     * @return
     */
    public long deleteData(int id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    /**
     * 删除所有数据
     * @return
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    /**
     * 更新一条数据
     * @param award
     * @return
     */
    public long updateData(Award award) {
        ContentValues values = new ContentValues();

        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(award.getTime()));
        values.put(KEY_CONTENT, award.getContent());
        values.put(KEY_POINT, award.getPoint());
        values.put(KEY_BUY_NUM, award.getBuyNum());
        values.put(KEY_CLASSIFICATION, award.getClassification());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(award.getId())});
    }

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    public List<Award> queryData(int id) {
        if (!DBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, null,
                KEY_ID + "=" + id , null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Award> queryDataList() {
//        if (!DBConfig.HaveData(mDatabase,TABLE_NAME)){
//            return null;
//        }
        Cursor results = mDatabase.query(TABLE_NAME, null,
                null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<Award> convertUtil(Cursor cursor) {
//        int resultCounts = cursor.getCount();
//        if (resultCounts == 0 || !cursor.moveToFirst()) {
//            return null;
//        }
        List<Award> mList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Award award = new Award();
            award.setId(cursor.getInt(0));

            String time = cursor.getString(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 将字符串解析为Date对象
            Date date = null;
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // 将Date对象转换为Timestamp对象
            Timestamp timestamp = new Timestamp(date.getTime());
            award.setTime(timestamp);

            award.setContent(cursor.getString(2));
            award.setPoint(cursor.getInt(3));
            award.setBuyNum(cursor.getInt(4));
            award.setClassification(cursor.getString(5));

            mList.add(award);
        }
        return mList;
    }

    public int getId() {
        Cursor cursor = mDatabase.rawQuery("select last_insert_rowid() from " + TABLE_NAME, null);
        int id = -1;
        if (cursor.moveToFirst())
            id = cursor.getInt(0);
        return id;
    }
}

