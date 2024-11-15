package com.example.activitylogger;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditActivityRecord extends AppCompatActivity {
    private ActivityRecordManager recordManager;
    private EditText editDistance, editDate, editCalory, editActivityType, editTime;
    private int recordId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        recordManager = new ActivityRecordManager(this);
        recordId = getIntent().getIntExtra("record_id", -1);
        Intent intent = getIntent();
        int a = intent.getIntExtra("a", -1);
        editDistance = findViewById(R.id.editDistance);
        editDate = findViewById(R.id.editDate);
        editActivityType = findViewById(R.id.editActivityType);
        editTime = findViewById(R.id.editTime);
        editCalory =findViewById(R.id.editCalory);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });

        if(a==-1){
            loadRecordData();
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a==-1){
                    saveRecord();
                }else if(a==1){
                    createNewRecord();
                }
            }
        });
    }

    private void loadRecordData() {
        ActivityRecord record = recordManager.getActivityRecordById(recordId);
        if (record != null) {
            editDistance.setText(record.get_distance());
            editDate.setText(record.get_date());
            editActivityType.setText(record.get_activity_type());
            editTime.setText(record.get_time());
            Log.d("recordcheck", record.get_distance());
        }else{
            Log.d("recordcheck", "nulldayon");
        }
    }

    private void saveRecord() {
        ActivityRecord record = new ActivityRecord(
                1,
                editDistance.getText().toString(),
                editDate.getText().toString(),
                "0",
                editActivityType.getText().toString(),
                editTime.getText().toString()
        );
        Log.d("EditActivityRecord", record.get_distance()+record.get_activity_type()+record.get_calory());
        record.set_id(recordId);
        record.set_calory("100");
        recordManager.updateActivityRecord(record);
        finish();
    }

    private void createNewRecord() {
        String distance = editDistance.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String calory = editCalory.getText().toString().trim();
        String activityType = editActivityType.getText().toString().trim();
        String time = editTime.getText().toString().trim(); // 入力チェック
        if (distance.isEmpty() || date.isEmpty() || calory.isEmpty() || activityType.isEmpty() || time.isEmpty()) { // 警告メッセージを表示
            Toast.makeText(this, "すべてのフィールドを入力してください", Toast.LENGTH_SHORT).show();
            return; // メソッドを終了し、保存処理を行わない
        }
        ActivityRecord newRecord = new ActivityRecord(
                -1, // 新しいレコードなのでIDは初期値
                distance,
                date,
                calory,
                activityType,
                time
        ); // 新しいレコードをデータベースに保存
        Log.d("CreateNewRecord", "Distance: " + distance); Log.d("CreateNewRecord", "Date: " + date); Log.d("CreateNewRecord", "Calory: " + calory); Log.d("CreateNewRecord", "Activity Type: " + activityType); Log.d("CreateNewRecord", "Time: " +time);
         recordManager.addActivityRecord(newRecord);
         // アクティビティを終了して前の画面に戻る
         finish();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                editDate.setText(selectedDate);
            }
            },
                year, month, day
        );
        datePickerDialog.show();
    }



}