package com.example.activitylogger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TargetListActivity extends AppCompatActivity {

    private ListView activityListView;
    private TargetDatabaseManager dbHelper;
    private List<TargetRecord> targetRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_list);

        activityListView = findViewById(R.id.ActivityList);
        dbHelper = new TargetDatabaseManager(this);

        // データベースからレコードを取得
        targetRecords = dbHelper.getAllTargetRecords();

        // デバッグログの追加
        Log.d("TargetListActivity", "Records retrieved: " + targetRecords.size());

        // リストビューにアダプターを設定
        TargetRecordAdapter adapter = new TargetRecordAdapter(this, targetRecords);
        activityListView.setAdapter(adapter);

        Button homebutton = findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TargetListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
