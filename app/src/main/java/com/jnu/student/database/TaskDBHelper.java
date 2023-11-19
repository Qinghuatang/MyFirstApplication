package com.jnu.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jnu.student.data.Task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PlayTask.db";
    private static final String TABLE_NAME = "task";
    private static final int DB_VERSION = 1;
    private static TaskDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private TaskDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static TaskDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new TaskDBHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    // 创建数据库, 执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "taskType TEXT CHECK(taskType IN ('Daily', 'Weekly', 'Common')) NOT NULL, " +
                "time DATETIME NOT NULL," +
                "content VARCHAR NOT NULL," +
                "point INTEGER NOT NULL," +
                "finishedNum INTEGER," +
                "num INTEGER," +
                "classification VARCHAR);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(Task task) {
        ContentValues values = new ContentValues();
        values.put("taskType", task.getTaskType());
        values.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getTime()));
        values.put("content", task.getContent());
        values.put("point", task.getPoint());
        values.put("finishedNum", task.getFinishedNum());
        values.put("num", task.getNum());
        values.put("classification", task.getClassification());


        return mWDB.insert(TABLE_NAME, null, values);
//        try {
//            mWDB.beginTransaction();
//
//            Log.d("test", "insert");
//            mWDB.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            mWDB.endTransaction();
//        }
    }

    public List<Task> queryAll() {
        List<Task> list = new ArrayList<>();
        // 执行记录查询动作, 该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            Task task = new Task();
            task.setTaskType(cursor.getString(1));

            String time = cursor.getString(2);
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
            task.setTime(timestamp);

            task.setContent(cursor.getString(3));
            task.setPoint(cursor.getInt(4));
            list.add(task);
        }
        return list;
    }

    public List<Task> queryByType(String... taskType) {
        List<Task> list = new ArrayList<>();
        String selection = "taskType = ?";
        String[] selectionArgs = taskType;
        // 执行记录查询动作, 该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            Task task = new Task();
            task.setId(cursor.getInt(0));
            task.setTaskType(cursor.getString(1));

            String time = cursor.getString(2);
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
            task.setTime(timestamp);

            task.setContent(cursor.getString(3));
            task.setPoint(cursor.getInt(4));
            task.setFinishedNum(cursor.getInt(5));
            task.setNum(cursor.getInt(6));
            task.setClassification(cursor.getString(7));
            list.add(task);
        }
        return list;
    }

    public void delTask(Task task) {
        mRDB.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(task.getId())});
    }

    public void updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("id", task.getId());
        values.put("taskType", task.getTaskType());
        values.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getTime()));
        values.put("content", task.getContent());
        values.put("point", task.getPoint());
        values.put("finishedNum", task.getFinishedNum());
        values.put("num", task.getNum());
        values.put("classification", task.getClassification());

        mRDB.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(task.getId())});
    }

    public int getId() {
        Cursor cursor = mRDB.rawQuery("select last_insert_rowid() from " + TABLE_NAME, null);
        int id = -1;
        if (cursor.moveToFirst())
            id = cursor.getInt(0);
        return id;
    }
}
