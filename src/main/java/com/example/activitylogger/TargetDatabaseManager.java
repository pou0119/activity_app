package com.example.activitylogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

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

    public List<TargetRecord> getAllTargetRecords() {
        List<TargetRecord> records = dbHelper.getAllTargetRecords();
        return records;
    }
}
