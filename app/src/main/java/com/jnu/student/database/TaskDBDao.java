package com.jnu.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jnu.student.data.Task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDBDao {
    /** 数据表名称 */
    public static final String TABLE_NAME = "task";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_TASK_TYPE = "taskType";
    public static String KEY_TIME = "time";
    public static String KEY_CONTENT = "content";
    public static String KEY_POINT = "point";
    public static String KEY_FINISHED_NUM = "finishedNum";
    public static String KEY_NUM = "num";
    public static String KEY_CLASSIFICATION = "classification";

    private SQLiteDatabase mDatabase;

    /** 上下文 */
    private Context mContext;

    /** 数据库打开帮助类 */
    private DBMaster.DBOpenHelper mDbOpenHelper;

    public TaskDBDao(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param task
     * @return
     */
    public long insertTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TYPE, task.getTaskType());
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getTime()));
        values.put(KEY_CONTENT, task.getContent());
        values.put(KEY_POINT, task.getPoint());
        values.put(KEY_FINISHED_NUM, task.getFinishedNum());
        values.put(KEY_NUM, task.getNum());
        values.put(KEY_CLASSIFICATION, task.getClassification());

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
     * @param task
     * @return
     */
    public long updateTask(Task task) {
        ContentValues values = new ContentValues();

        values.put(KEY_TASK_TYPE, task.getTaskType());
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getTime()));
        values.put(KEY_CONTENT, task.getContent());
        values.put(KEY_POINT, task.getPoint());
        values.put(KEY_FINISHED_NUM, task.getFinishedNum());
        values.put(KEY_NUM, task.getNum());
        values.put(KEY_CLASSIFICATION, task.getClassification());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + task.getId(), null);
    }

    /**
     * 查询某一类型的任务
     * @param taskType
     * @return
     */
    public List<Task> queryTaskByType(String... taskType) {
//        if (!DBConfig.HaveData(mDatabase,TABLE_NAME)){
//            return null;
//        }
        String selection = "taskType = ?";
        String[] selectionArgs = taskType;
        Cursor results = mDatabase.query(TABLE_NAME, null,
                selection , selectionArgs, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有任务
     * @return
     */
    public List<Task> queryAllTask() {
        if (!DBConfig.HaveData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, null,
                null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<Task> convertUtil(Cursor cursor) {
//        int resultCounts = cursor.getCount();
//        if (resultCounts == 0 || !cursor.moveToFirst()) {
//            return null;
//        }
        List<Task> mList = new ArrayList<>();
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

            mList.add(task);
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
