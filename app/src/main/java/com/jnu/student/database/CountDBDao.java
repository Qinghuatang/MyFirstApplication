package com.jnu.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.jnu.student.data.Award;
import com.jnu.student.data.Count;


import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;

public class CountDBDao {
    /** 数据表名称 */
    public static final String TABLE_NAME = "count";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_DATE = "date";
    public static String KEY_CONTENT = "content";
    public static String KEY_POINT = "point";
    public static String KEY_CLASSIFICATION = "classification";
    private String year;    // 记录当前年份
    private String month;   // 记录当前月份

    private SQLiteDatabase mDatabase;
    /** 上下文 */
    private Context mContext;
    /** 数据库打开帮助类 */
    private DBMaster.DBOpenHelper mDbOpenHelper;

    public CountDBDao(Context context) {
        mContext = context;
        Calendar calendar = Calendar.getInstance();
        this.year = String.valueOf(calendar.get(Calendar.YEAR));
        this.month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        Log.d("month", month);
    }

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param count
     * @return
     */
    public long insertData(Count count) {
        ContentValues values = new ContentValues();
        values.put(KEY_POINT, count.getPoint());
        values.put(KEY_CONTENT, count.getContent());
        values.put(KEY_CLASSIFICATION, count.getClassification());

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
     * @param count
     * @return
     */
    public long updateData(Count count) {
        ContentValues values = new ContentValues();
        values.put(KEY_POINT, count.getPoint());
        values.put(KEY_CONTENT, count.getContent());
        values.put(KEY_CLASSIFICATION, count.getClassification());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + count.getId(), null);
    }

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    public List<Count> queryData(int id) {
//
        Cursor results = mDatabase.query(TABLE_NAME, null,
                KEY_ID + "=" + id , null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Count> queryDataListByDate(String date) {

        Cursor results = mDatabase.query(TABLE_NAME, null,
                "date = ?", new String[]{date}, null, null, null);
        return convertUtil(results);
    }

    public int queryPointSum() {
        int pointSum = 0;
        String sql = "select SUM(point) as total from count";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            pointSum = cursor.getInt(0);
        }
        return pointSum;
    }

    public int queryDayIncomeByDate(String date){
        int income = 0;

        String sql = "select SUM(point) as total from count  where point > 0 and date = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{date});

        if (cursor.moveToFirst()) {
            income = cursor.getInt(0);
        }

        return income;
    }
    public int queryDayExpendByDate(String date){
        int expend = 0;

        String sql = "select SUM(point) as total from count  where point < 0 and date = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{date});

        if (cursor.moveToFirst()) {
            expend = cursor.getInt(0);
        }

        return -expend;
    }

    public List<String> queryDateByMonth(int month){
        String query = "SELECT DISTINCT DATE(" + KEY_DATE + ") AS unique_dates FROM " + TABLE_NAME +
                " WHERE strftime('%m', date) = ?";
        Cursor results = mDatabase.rawQuery(query, new String[]{String.valueOf(month)});
        ArrayList<String> dateList = new ArrayList<>();
        while (results.moveToNext()) {
            String uniqueDate = results.getString(0);
            dateList.add(uniqueDate);
        }
        return dateList;
    }


    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<Count> convertUtil(Cursor cursor) {
        List<Count> mList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Count count = new Count();
            count.setId(cursor.getInt(0));

            String dateStr = cursor.getString(1);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // 将字符串解析为Date对象
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            count.setDate(new java.sql.Date(date.getTime()));
            count.setPoint(cursor.getInt(2));
            count.setContent(cursor.getString(3));
            count.setClassification(cursor.getString(4));

            mList.add(count);
        }
        return mList;
    }

    // 查询一年中每一月份的收入总和
    public List<BarEntry> queryYearIncome(){
        List<BarEntry> entries = new ArrayList<>();
        String[] columns = new String[]{"strftime('%m', date)", "SUM(point)"};
        String selection = "strftime('%Y', date) = ? and point > 0";
        String[] selectionArgs = new String[]{this.year};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "strftime('%m', date)", null, null);
        while (results.moveToNext()) {
            int month = results.getInt(0) - 1;
            int monthIncome = results.getInt(1);
            BarEntry entry = new BarEntry(month, monthIncome);
            entries.add(entry);
        }
        return entries;
    }

    public List<BarEntry> queryYearExpend(){
        List<BarEntry> entries = new ArrayList<>();
        String[] columns = new String[]{"strftime('%m', date)", "-SUM(point)"};
        String selection = "strftime('%Y', date) = ? and point < 0";
        String[] selectionArgs = new String[]{this.year};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "strftime('%m', date)", null, null);
        while (results.moveToNext()) {
            int month = results.getInt(0) - 1;
            int monthExpend = results.getInt(1);
            BarEntry entry = new BarEntry(month, monthExpend);
            entries.add(entry);
        }
        return entries;
    }


    public List<Entry> queryMonthIncome(){
        List<Entry> entries = new ArrayList<>();
        String[] columns = new String[]{"CAST(strftime('%d', date) AS INTEGER)", "SUM(point)"};
        String selection = "strftime('%m', date) = ? and point > 0";
        String month = String.valueOf(this.month);
        String[] selectionArgs = new String[]{month};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "strftime('%d', date)", null, null);
        while (results.moveToNext()) {
            int day = results.getInt(0) - 1;
            int dayIncome = results.getInt(1);
            Log.d("dayIncome", day + " " + dayIncome);
            Entry entry = new Entry(day, dayIncome);
            entries.add(entry);
        }
        return entries;
    }

    public List<Entry> queryMonthExpend(){
        List<Entry> entries = new ArrayList<>();
        String[] columns = new String[]{"CAST(strftime('%d', date) AS INTEGER)", "-SUM(point)"};
        String selection = "strftime('%m', date) = ? and point < 0";
        String[] selectionArgs = new String[]{this.month};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "strftime('%d', date)", null, null);
        while (results.moveToNext()) {
            int day = results.getInt(0) - 1;
            int dayExpend = results.getInt(1);
            Entry entry = new Entry(day, dayExpend);
            entries.add(entry);
        }
        return entries;
    }

    public List<Entry> queryWeekIncome(){
        List<Entry> entries = new ArrayList<>();
        String[] columns = new String[]{
                "CAST(strftime('%j', date) - strftime('%j', 'now', 'weekday 0', '-6 days') AS INTEGER) AS days_to_start_of_week",
                "SUM(point) AS total_points"
                };
        String selection = "date >= date('now', 'weekday 0', '-6 days') AND date < date('now', 'weekday 0', '+1 day')"
                + "and point > 0";
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , null, "date", null, "date");
        while (results.moveToNext()) {
            int day = results.getInt(0);
            int dayIncome = results.getInt(1);
            Entry entry = new Entry(day, dayIncome);
            entries.add(entry);
        }
        return entries;
    }

    public List<Entry> queryWeekExpend(){
        List<Entry> entries = new ArrayList<>();
        String[] columns = new String[]{
                "CAST(strftime('%j', date) - strftime('%j', 'now', 'weekday 0', '-6 days') AS INTEGER) AS days_to_start_of_week",
                "-SUM(point) AS total_points"
        };
        String selection = "date >= date('now', 'weekday 0', '-6 days') AND date < date('now', 'weekday 0', '+1 day')"
                + "and point < 0";
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , null, "date", null, "date");
        while (results.moveToNext()) {
            int day = results.getInt(0);
            int dayIncome = results.getInt(1);
            Entry entry = new Entry(day, dayIncome);
            entries.add(entry);
        }
        return entries;
    }

    public ArrayList<PieEntry> queryClassMonthIncome(int month){
        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] columns = new String[]{"classification", "SUM(point)"};
        String selection = "strftime('%m', date) = ? and point > 0";
        String[] selectionArgs = new String[]{String.valueOf(month)};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "classification", null, null);
        while (results.moveToNext()) {
            String classification = results.getString(0);
            int income = results.getInt(1);
            PieEntry entry = new PieEntry(income, classification);
            entries.add(entry);
        }
        return entries;
    }

    public ArrayList<PieEntry> queryClassMonthExpend(int month){
        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] columns = new String[]{"classification", "-SUM(point)"};
        String selection = "strftime('%m', date) = ? and point < 0";
        String[] selectionArgs = new String[]{String.valueOf(month)};
        Cursor results = mDatabase.query(TABLE_NAME, columns,
                selection , selectionArgs, "classification", null, null);
        while (results.moveToNext()) {
            String classification = results.getString(0);
            int income = results.getInt(1);
            PieEntry entry = new PieEntry(income, classification);
            entries.add(entry);
        }
        return entries;
    }


    public int getId() {
        Cursor cursor = mDatabase.rawQuery("select last_insert_rowid() from " + TABLE_NAME, null);
        int id = -1;
        if (cursor.moveToFirst())
            id = cursor.getInt(0);
        return id;
    }
}
