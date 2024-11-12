package com.example.activitylogger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

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

        editDistance = findViewById(R.id.editDistance);
        editDate = findViewById(R.id.editDate);
        editCalory = findViewById(R.id.editCalory);
        editActivityType = findViewById(R.id.editActivityType);
        editTime = findViewById(R.id.editTime);

        loadRecordData();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });
    }

    private void loadRecordData() {
        ActivityRecord record = recordManager.getActivityRecordById(recordId);
        if (record != null) {
            editDistance.setText(record.get_distance());
            editDate.setText(record.get_date());
            editCalory.setText(record.get_calory());
            editActivityType.setText(record.get_activity_type());
            editTime.setText(record.get_time());
            Log.d("recordcheck", "nulljanaiyon");
        }else{
            Log.d("recordcheck", "nulldayon");
        }
    }

    private void saveRecord() {
        ActivityRecord record = new ActivityRecord(
                1,
                editDistance.getText().toString(),
                editDate.getText().toString(),
                editCalory.getText().toString(),
                editActivityType.getText().toString(),
                editTime.getText().toString()
        );
        record.set_id(recordId);
        recordManager.updateActivityRecord(record);
        finish();
    }
}