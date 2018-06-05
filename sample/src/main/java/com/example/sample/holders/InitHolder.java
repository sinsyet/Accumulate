package com.example.sample.holders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sample.R;
import com.example.sample.async.initer.AbsIniter;


public class InitHolder extends ViewHolder<AbsIniter> implements AbsIniter.OnInitProgressListener {
    private static final String TAG = "InitHolder";
    private View mPb;
    private TextView mTvDesc;
    private TextView mTvProgress;

    public InitHolder(Context ctx) {
        super(ctx);
    }

    @Override
    protected View onCreateView() {
        return View.inflate(getContext(), R.layout.item_zzwinit, null);
    }

    @Override
    protected void onViewCreated(View v) {
        super.onViewCreated(v);
        mPb = v.findViewById(R.id.zzwinit_pb);
        mTvDesc = v.findViewById(R.id.zzwinit_tv_desc);
        mTvProgress = v.findViewById(R.id.zzwinit_tv_progress);
    }

    @Override
    public void bindData(AbsIniter data) {
        int initType = data.getInitType();
        switch (initType){
            case AbsIniter.TYPE_BASE:
                mPb.setVisibility(View.VISIBLE);
                mTvProgress.setVisibility(View.INVISIBLE);
                break;
            case AbsIniter.TYPE_PROGRESS:
                mTvProgress.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.INVISIBLE);
                data.setOnInitProgressListener(this);
                mTvProgress.setText(((int)data.getCurFrequency()) + "%");
                break;
        }

        int initState = data.getInitState();
        switch (initState){
            case AbsIniter.STATE_INIT:
                mTvDesc.setTextColor(0x0000FF);
                break;
            case AbsIniter.STATE_FAIL:
                // break;
            case AbsIniter.STATE_EXCEPTION:
                mTvDesc.setTextColor(0xff0000);
                break;
            case AbsIniter.STATE_SUCCESS:
                mTvDesc.setTextColor(0x00ff00);
                break;
        }

        String text = data.getHintExtra().toString();
        /*mTvDesc.setText(text);
        mTvDesc.setVisibility(View.VISIBLE);
        mTvDesc.setTextColor(0x000000);*/
        Log.e(TAG, "bindData: "+mTvDesc.getText()+" // "+mTvDesc.getHeight());

    }

    private final String FORMAT = "%d%";
    @Override
    public void onProgress(float total, float progress, float frequency) {
        mTvProgress.setText(((int)frequency) + "%");

        if(frequency >= 1.0f){
            mTvDesc.setTextColor(0x00ff00);
        }
    }
}
