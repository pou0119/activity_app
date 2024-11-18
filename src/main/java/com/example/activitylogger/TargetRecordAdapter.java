package com.example.activitylogger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class TargetRecordAdapter extends ArrayAdapter<TargetRecord> {
    public TargetRecordAdapter(Context context, List<TargetRecord> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Viewが再利用されていない場合、新しく作成
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_target_record, parent, false);
        }

        // 対象のレコードを取得
        TargetRecord record = getItem(position);
        Log.d("checktarget", record.getTarget());

        // UIコンポーネントの参照を取得
        TextView targetNameTextView = convertView.findViewById(R.id.targetName);
        TextView wishNameTextView = convertView.findViewById(R.id.wishName);

        // テキストを設定
        targetNameTextView.setText("目標距離:"+record.getTarget());
        wishNameTextView.setText("ほしいもの:"+record.getWishName());

        return convertView;
    }
}
