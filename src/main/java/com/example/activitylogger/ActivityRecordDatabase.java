// ファイル名: ActivityRecordDatabase.java
package com.example.activitylogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityRecordDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "activitydatabase.db";
    private static final int DATABASE_VERSION = 20;

    public static final String TABLE_NAME = "activity";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CALORY = "calory";
    //    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    //    public static final String COLUMN_USER_HEIGHT = "user_height";
//    public static final String COLUMN_USER_WEIGHT = "user_weight";
//    public static final String COLUMN_USER_AGE = "user_age";
//    public static final String COLUMN_USER_SEX = "user_sex";
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, " + COLUMN_DISTANCE + " TEXT, " + COLUMN_TIME + " TEXT, " + COLUMN_CALORY + " TEXT, " + COLUMN_ACTIVITY_TYPE + " TEXT);";

    public ActivityRecordDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); onCreate(db); }


    public void addActivityRecord(ActivityRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTANCE, record.get_distance());
        values.put(COLUMN_TIME, record.get_time());
        values.put(COLUMN_CALORY, record.get_calory());
        values.put(COLUMN_DATE, record.get_date());
//        values.put(COLUMN_SPEED, record.get_speed().toString());
        values.put(COLUMN_ACTIVITY_TYPE, record.get_activity_type());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public ActivityRecord getActivityRecordById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,   // テーブル名
                new String[]{COLUMN_ID, COLUMN_DATE,COLUMN_DISTANCE, COLUMN_TIME, COLUMN_CALORY, COLUMN_ACTIVITY_TYPE}, // 取得する列
                COLUMN_ID + "=?",               // WHERE句
                new String[]{String.valueOf(id)}, // WHERE句の引数
                null,          // GROUP BY句
                null,          // HAVING句
                null,          // ORDER BY句
                null);         // LIMIT句

        if (cursor != null) {
            cursor.moveToFirst();
            ActivityRecord record = new ActivityRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CALORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

            );
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    public List<ActivityRecord> getAllActivityRecords() {
        List<ActivityRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ActivityRecord record = new ActivityRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CALORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                );
                records.add(record);
            }
            cursor.close();
        }
        return records;
    }
    public void deleteActivityRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void updateActivityRecord(ActivityRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTANCE, record.get_distance());
        values.put(COLUMN_DATE, record.get_date());
        values.put(COLUMN_CALORY, record.get_calory());
        values.put(COLUMN_ACTIVITY_TYPE, record.get_activity_type());
        values.put(COLUMN_TIME, record.get_time());
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(record.get_id())});
        db.close();
    }
}



