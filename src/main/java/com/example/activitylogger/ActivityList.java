package com.example.activitylogger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ActivityList extends AppCompatActivity {
    private ActivityRecordManager recordManager;
    private ListView activityListView;
    private ActivityRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recordManager = new ActivityRecordManager(this);
        activityListView = findViewById(R.id.ActivityList);

        List<ActivityRecord> records = recordManager.getAllActivityRecords();
        // 日付の新しい順にソート
        Collections.sort(records, new Comparator<ActivityRecord>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            @Override public int compare(ActivityRecord record1, ActivityRecord record2) {
                try { Date date1 = dateFormat.parse(record1.getDate());
                    Date date2 = dateFormat.parse(record2.getDate());
                    return date2.compareTo(date1);
                    // 新しい日付が先に来るようにソート
                    } catch (ParseException e) { e.printStackTrace();
                    return 0; } } });

        adapter = new ActivityRecordAdapter(this, records);
        activityListView.setAdapter(adapter);

        Button buttonback = findViewById(R.id.homebutton);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}