package com.example.activitylogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TargetDatabaseManager {

    private TargetRecordDatabese dbHelper;
    private SQLiteDatabase database;

    public TargetDatabaseManager(Context context) {
        dbHelper = new TargetRecordDatabese(context);
    }

    public void addTargetRecord(TargetRecord record) {
        dbHelper.addTargetRecord(record);
    }
    public TargetRecord getTargetRecordById(int id) {
        TargetRecord targetrecord = dbHelper.getTargetRecordById(id);
        return targetrecord;
    }
}
