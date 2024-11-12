package com.example.activitylogger;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private String activity_type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonEdit = findViewById(R.id.button);
        Button buttonGoal = findViewById(R.id.button2);
        Button buttonRunning = findViewById(R.id.running);
        Button buttonWalking = findViewById(R.id.walking);
        Button buttonstart=findViewById(R.id.startbutton);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityList.class);
                startActivity(intent);
            }
        });


        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity_type.equals("ランニング")){
                    activity_type = "";
                    buttonRunning.setBackgroundColor(Color.parseColor("#6200EE"));
                    buttonWalking.setBackgroundColor(Color.parseColor("#6200EE"));

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
                    buttonWalking.setBackgroundColor(Color.parseColor("#6200EE"));
                    buttonRunning.setBackgroundColor(Color.parseColor("#6200EE"));
                }else{
                    activity_type = "ウォーキング";
                    buttonWalking.setBackgroundColor(Color.GREEN);
                    buttonRunning.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (activity_type.equals("ランニング")||activity_type.equals("ウォーキング")){
                    intent = new Intent(MainActivity.this, ActivityAction.class);
                }else{
                    Toast.makeText(MainActivity.this, "アクティビティを選択してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("activity_type", activity_type);
                startActivity(intent);
            }
        });
    }
}
