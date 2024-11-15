package com.example.activitylogger;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
    private String  activity_type="";
    Button buttonRunning ;
    Button buttonWalking ;

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
        editTime = findViewById(R.id.editTime);
        Button backbutton = findViewById(R.id.canselButton);
        buttonRunning=findViewById(R.id.runningbutton);
        buttonWalking=findViewById(R.id.walkingbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivityRecord.this, ActivityList.class);
                startActivity(intent);

            }
        });


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

        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity_type.equals("ランニング")){
                    activity_type = "";
                    buttonRunning.setBackgroundColor(Color.LTGRAY);
                    buttonWalking.setBackgroundColor(Color.LTGRAY);

                }else{
                    activity_type = "ランニング";
                    buttonRunning.setBackgroundColor(Color.GREEN);
                    buttonWalking.setBackgroundColor(Color.LTGRAY);

                }

            }
        });

        buttonWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity_type.equals("ウォーキング")){
                    activity_type = "";
                    buttonWalking.setBackgroundColor(Color.LTGRAY);
                    buttonRunning.setBackgroundColor(Color.LTGRAY);
                }else{
                    activity_type = "ウォーキング";
                    buttonWalking.setBackgroundColor(Color.GREEN);
                    buttonRunning.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    private void loadRecordData() {
        ActivityRecord record = recordManager.getActivityRecordById(recordId);
        if (record != null) {
            editDistance.setText(record.get_distance());
            editDate.setText(record.get_date());
            if (record.get_activity_type().equals("ランニング")){
                activity_type="ランニング";
                buttonRunning.setBackgroundColor(Color.GREEN);
                buttonWalking.setBackgroundColor(Color.LTGRAY);
            }else if(record.get_activity_type().equals("ウォーキング")){
                activity_type="ウォーキング";
                buttonWalking.setBackgroundColor(Color.GREEN);
                buttonRunning.setBackgroundColor(Color.LTGRAY);
            }
            editTime.setText(record.get_time());
            Log.d("recordcheck", record.get_activity_type());
        }else{
            Log.d("recordcheck", "nulldayon");
        }
    }

    private void saveRecord() {
        String distance = editDistance.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String activityType = activity_type;
        String timeInput = editTime.getText().toString().trim();

        // 入力がすべて揃っているかチェック
        if (distance.isEmpty() || date.isEmpty() || timeInput.isEmpty() || activityType.isEmpty()) {
            Toast.makeText(this, "すべてのフィールドを入力してください", Toast.LENGTH_SHORT).show();
            return; // 入力が不完全な場合は保存処理を行わない
        }

        // timeInputをミリ秒としてパース
        long timeInMs = Long.parseLong(timeInput);
        // ミリ秒を時間に変換
        double timeInHours = timeInMs / (1000.0 * 60 * 60);

        // カロリーを計算
        double calory = 8 * 60 * timeInHours;
        String caloryString = String.format("%.1f", calory);

        ActivityRecord record = new ActivityRecord(
                1,
                distance,
                date,
                caloryString,
                activityType,
                timeInput
        );
        Log.d("EditActivityRecord", record.get_distance() + record.get_activity_type() + record.get_calory());
        record.set_id(recordId);
        record.set_calory(caloryString);
        recordManager.updateActivityRecord(record);
        finish();
    }



    private void createNewRecord() {
        String distance = editDistance.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String activityType = activity_type;
        String time = editTime.getText().toString().trim(); // 入力チェック
        if (distance.isEmpty() || date.isEmpty()|| activityType.isEmpty() || time.isEmpty()) { // 警告メッセージを表示
            Toast.makeText(this, "すべてのフィールドを入力してください", Toast.LENGTH_SHORT).show();
            return; // メソッドを終了し、保存処理を行わない
        }
        ActivityRecord newRecord = new ActivityRecord(
                -1, // 新しいレコードなのでIDは初期値
                distance,
                date,
                "100",
                activityType,
                time
        ); // 新しいレコードをデータベースに保存
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