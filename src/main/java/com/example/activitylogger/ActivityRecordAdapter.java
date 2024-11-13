package com.example.activitylogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ActivityRecordAdapter extends ArrayAdapter<ActivityRecord> {
    private Context context;
    private List<ActivityRecord> records;
    private ActivityRecordManager dbHelper;

    public ActivityRecordAdapter(Context context, List<ActivityRecord> records) {

        super(context, 0, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_activity_record, parent, false);
        }

        ActivityRecord record = records.get(position);

        TextView distanceTextView = convertView.findViewById(R.id.distanceTextView);
        TextView caloryTextView = convertView.findViewById(R.id.caloryTextView);
        TextView activityTypeTextView = convertView.findViewById(R.id.activityTypeTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);

        distanceTextView.setText("距離："+record.get_distance()+"km");
        caloryTextView.setText("カロリー:" + record.get_calory() + "kcal" );
        activityTypeTextView.setText(record.get_activity_type());
        timeTextView.setText(formatElapsedTime(record.get_time()));
        dateTextView.setText(record.get_date());

        return convertView;
    }

    private String formatElapsedTime(String elapsedTimeStr) {
        float elapsedTime = Float.parseFloat(elapsedTimeStr);
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
        int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);
        int milliseconds = (int) (elapsedTime % 1000);
        return String.format("時間:%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds);
    }

}
