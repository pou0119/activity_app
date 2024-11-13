package com.example.activitylogger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
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
    private List<ActivityRecord> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recordManager = new ActivityRecordManager(this);
        activityListView = findViewById(R.id.ActivityList);

        loadRecords();

        activityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionsDialog(position);
                return true;
            }
        });

        Button buttonback = findViewById(R.id.homebutton);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadRecords() {
        records = recordManager.getAllActivityRecords();
        // 日付の新しい順にソート
        Collections.sort(records, new Comparator<ActivityRecord>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public int compare(ActivityRecord record1, ActivityRecord record2) {
                try {
                    Date date1 = dateFormat.parse(record1.getDate());
                    Date date2 = dateFormat.parse(record2.getDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        adapter = new ActivityRecordAdapter(this, records);
        activityListView.setAdapter(adapter);
    }

    private void showOptionsDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("オプション")
                .setItems(new CharSequence[]{"編集", "削除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 編集
                                editRecord(position);
                                break;
                            case 1: // 削除
                                deleteRecord(position);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void editRecord(int position) {
        ActivityRecord record = records.get(position);
        Intent intent = new Intent(ActivityList.this, EditActivityRecord.class);
        intent.putExtra("record_id", record.get_id());
        startActivity(intent);
    }



    private void deleteRecord(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("確認")
                .setMessage("本当に削除しますか？")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityRecord record = records.get(position);
                        if (record != null) {
                            Integer recordId = record.get_id(); // ここで`Integer`オブジェクトを取得
                            if (recordId != null) {
                                recordManager.deleteActivityRecord(record.get_id());
                                records.remove(position);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.e("ActivityList", "Record ID is null");
                            }
                        } else {
                            Log.e("ActivityList", "Record is null");
                        }
                    }
                })
                .setNegativeButton("いいえ", null)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(); // アクティビティが再開されたときにレコードを再読み込み
    }
}