package com.example.activitylogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityAction extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    private Location previousLocation;
    private float totalDistance = 0;
    private long startTime;
    private long pausedTime = 0; // 一時停止時の経過時間を保存する変数
    private String distancesam;
    private String calory_data;
    private long elapsedTime;
    private Handler handler = new Handler();
    private boolean isPaused = false; // フラグを追加
    private  String activity_title;
    private ActivityRecordManager dbHelper;
    private ActivityRecord record;
    private String formattedDate;
    private int cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra("activity_type");
        activity_title = String.format("アクティビティ：%s",activityType);
        dbHelper = new ActivityRecordManager(this);
        record = new ActivityRecord();

//        日付の取得
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = formatter.format(calendar.getTime());

        locationTextView = findViewById(R.id.activityView);
        Button buttonstop = findViewById(R.id.stop);
        Button buttonsstop = findViewById(R.id.sstop);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("ActivityAction", "Location changed: " + location.toString());
                updateLocation(location);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocation(lastKnownLocation);
            }
        }

        startTime = System.currentTimeMillis(); // スタート時間を初期化

        // 時間の更新をコンマ一秒ごとに行う
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 100); // 100ミリ秒ごとに更新
            }
        }, 100);

        buttonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.set_distance(distancesam);
                record.set_calory(calory_data);
                record.set_activity_type(activityType);
                record.set_time(String.valueOf(elapsedTime));
                record.set_date(formattedDate);
                dbHelper.addActivityRecord(record);
                Intent intent = new Intent(ActivityAction.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonsstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    // 再開処理
                    startTime = System.currentTimeMillis() - pausedTime; // 再開時に経過時間を考慮
                    startLocationUpdates();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateTime();
                            handler.postDelayed(this, 100); // 100ミリ秒ごとに更新
                        }
                    }, 100);
                    buttonsstop.setText("一時停止");
                } else {
                    // 一時停止処理
                    pausedTime = System.currentTimeMillis() - startTime; // 一時停止時の経過時間を保存
                    handler.removeCallbacksAndMessages(null); // 時間の更新を停止
                    locationManager.removeUpdates(locationListener); // GPSの更新を停止
                    buttonsstop.setText("再開");
                }
                isPaused = !isPaused; // フラグを反転
            }
        });
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, locationListener);
        }
    }

    private void updateLocation(Location location) {
        if (previousLocation != null) {
            float distance = previousLocation.distanceTo(location);
            if(distance>1){
                cal=1;
            }else{
                cal=0;
            }
            totalDistance += distance;
        }
        previousLocation = location;

        // 距離をキロメートル単位で表示
        String distanceText = String.format("%.1f", totalDistance / 1000);
        distancesam = distanceText;
    }

    private void updateTime() {
        elapsedTime = System.currentTimeMillis() - startTime;
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
        int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);
        int milliseconds = (int) (elapsedTime % 1000);
        float calory=0;
        if (activity_title.contains("ランニング")&&cal==1){
            calory = seconds*8.1f*60/3600;
        }else if(activity_title.contains("ウォーキング")&&cal==1){
            calory = seconds*3.1f*60/3600;
        }

        calory_data = String.format("%.1f", calory);
        String timeText = String.format("時間: %02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds);
        locationTextView.setText(formattedDate+"\n" +activity_title+ "\n" +"距離："+distancesam +"km"+ "\n" + timeText+"\n"+"カロリー:"+calory_data+"kcal"); // 時間を追加
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }
}
