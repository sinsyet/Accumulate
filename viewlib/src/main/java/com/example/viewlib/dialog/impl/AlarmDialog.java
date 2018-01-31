package com.example.viewlib.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.viewlib.R;
import com.example.viewlib.dialog.base.RxDialog;


public class AlarmDialog extends RxDialog implements View.OnClickListener {

    private View root;
    private TextView mTvAlaramType;
    private TextView mTvAlaramDate;
    private TextView mTvAlaramIKnow;
    private TextView mTvAlaramLiveinfo;

    public AlarmDialog(Context context) {
        super(context);
        initView();
        setWrapScreenHeightSize();
    }

    private void initView(){
        root = View.inflate(getContext(), R.layout.dialog_alarm, null);

        mTvAlaramType = (TextView) root.findViewById(R.id.tv_alarm_alarmtype);
        mTvAlaramDate = (TextView) root.findViewById(R.id.tv_alarm_date);
        mTvAlaramIKnow = (TextView) root.findViewById(R.id.tv_alarm_iknown);
        mTvAlaramLiveinfo = (TextView) root.findViewById(R.id.tv_alarm_liveinfo);

        mTvAlaramIKnow.setOnClickListener(this);
        setContentView(root);
    }

    public void setAlarmDate(CharSequence text){
        mTvAlaramDate.setText(text);
    }

    public void setAlarmType(CharSequence text){
        mTvAlaramType.setText(text);
    }

    public void setAlarmLiveInfo(CharSequence text){
        mTvAlaramLiveinfo.setText(text);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
