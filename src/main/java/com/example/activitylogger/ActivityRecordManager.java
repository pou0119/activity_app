package com.example.activitylogger;

import android.content.Context;

import java.util.List;

public class ActivityRecordManager {
    private ActivityRecordDatabase dbHelper;

    public ActivityRecordManager(Context context) {
        dbHelper = new ActivityRecordDatabase(context);
    }

    public void addActivityRecord(ActivityRecord record) {
        dbHelper.addActivityRecord(record);
    }
    // 他のデータベース操作メソッドもここに追加できます

    public ActivityRecord getActivityRecordById(long id) {
        ActivityRecord record = dbHelper.getActivityRecordById(id);
        return record;
    }
    public List<ActivityRecord> getAllActivityRecords() {
        List<ActivityRecord> records = dbHelper.getAllActivityRecords();
        return records;
    }

}
