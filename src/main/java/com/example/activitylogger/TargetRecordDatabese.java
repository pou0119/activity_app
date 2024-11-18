package com.example.activitylogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TargetRecordDatabese extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "targets.db";
    private static final int DATABASE_VERSION = 2;

    // テーブルとカラムの定義
    public static final String TABLE_TARGETS = "targets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TARGET = "target";
    public static final String COLUMN_TARGET_SET_ACCOMPLISHED = "targetSetAccomplished";
    public static final String COLUMN_WISH_NAME = "wishName";
    public static final String COLUMN_WISH_IMAGE = "wishImage";
    public static final String COLUMN_TARGET_MAIN="TargetMain";

    // テーブル作成クエリ
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TARGETS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TARGET + " TEXT, " +
                    COLUMN_TARGET_SET_ACCOMPLISHED + " INTEGER, " +
                    COLUMN_WISH_NAME + " TEXT, " +
                    COLUMN_WISH_IMAGE + " TEXT, " +
                    COLUMN_TARGET_MAIN + " TEXT" + // 修正
                    ");";

    public TargetRecordDatabese(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGETS);
        onCreate(db);
    }

    // レコードの追加
    public void addTargetRecord(TargetRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TARGET, record.getTarget());
        values.put(COLUMN_TARGET_SET_ACCOMPLISHED, record.isTargetSetAccomplished() ? 1 : 0);
        values.put(COLUMN_WISH_NAME, record.getWishName());
        values.put(COLUMN_WISH_IMAGE, record.getWishImage());
        values.put(COLUMN_TARGET_MAIN, record.gettargetmain());

        long id = db.insert(TABLE_TARGETS, null, values);
        db.close();
    }

    // レコードの取得（ID指定）
    public TargetRecord getTargetRecordById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TARGETS,
                new String[]{COLUMN_ID, COLUMN_TARGET, COLUMN_TARGET_SET_ACCOMPLISHED, COLUMN_WISH_NAME, COLUMN_WISH_IMAGE, COLUMN_TARGET_MAIN},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            TargetRecord record = new TargetRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TARGET_SET_ACCOMPLISHED)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WISH_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WISH_IMAGE)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET_MAIN)))
            );
            cursor.close();
            db.close();
            return record;
        } else {
            db.close();
            return null;
        }
    }

    // レコードの更新
    public int updateTargetRecord(TargetRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TARGET, record.getTarget());
        values.put(COLUMN_TARGET_SET_ACCOMPLISHED, record.isTargetSetAccomplished() ? 1 : 0);
        values.put(COLUMN_WISH_NAME, record.getWishName());
        values.put(COLUMN_WISH_IMAGE, record.getWishImage());
        values.put(COLUMN_TARGET_MAIN, record.gettargetmain());

        int rowsAffected = db.update(TABLE_TARGETS, values, COLUMN_ID + "=?", new String[]{String.valueOf(record.getId())});
        db.close();
        return rowsAffected;
    }

    // レコードの削除
    public void deleteTargetRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TARGETS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // すべてのレコードの取得
    public List<TargetRecord> getAllTargetRecords() {
        List<TargetRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TARGETS,
                new String[]{COLUMN_ID, COLUMN_TARGET, COLUMN_TARGET_SET_ACCOMPLISHED, COLUMN_WISH_NAME, COLUMN_WISH_IMAGE, COLUMN_TARGET_MAIN},
                null, null, null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                TargetRecord record = new TargetRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TARGET_SET_ACCOMPLISHED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WISH_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WISH_IMAGE)),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET_MAIN)))
                );
                records.add(record);
            }
            cursor.close();
        }
        db.close();
        return records;
    }
}
