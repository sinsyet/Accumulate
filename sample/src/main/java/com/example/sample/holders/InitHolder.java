package com.example.sample.holders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sample.R;
import com.example.sample.async.initer.AbsIniter;

import java.io.PrintWriter;
import java.io.StringWriter;


public class InitHolder extends ViewHolder<AbsIniter> implements AbsIniter.OnInitProgressListener {
    private static final String TAG = "InitHolder";
    private View mPb;
    private TextView mTvDesc;
    private TextView mTvProgress;
    private TextView mTvResult;
    private View mViewFl;

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
        mTvResult = v.findViewById(R.id.zzwinit_tv_result);
        mViewFl = v.findViewById(R.id.zzwinit_fl_init);
    }

    @Override
    public void bindData(AbsIniter data) {
        int initType = data.getInitType();
        String text = data.getHintExtra().toString();
        mTvDesc.setText(text);
        int initState = data.getInitState();
        if(initState != AbsIniter.STATE_INIT){
            mTvResult.setVisibility(View.VISIBLE);
            mViewFl.setVisibility(View.INVISIBLE);
        }else{
            mViewFl.setVisibility(View.VISIBLE);
            mTvResult.setVisibility(View.GONE);
        }
        switch (initState){
            case AbsIniter.STATE_INIT:
                mTvDesc.setTextColor(0xFF000000);
                switch (initType){
                    case AbsIniter.TYPE_BASE:
                        mPb.setVisibility(View.VISIBLE);
                        mTvProgress.setVisibility(View.INVISIBLE);
                        break;
                    case AbsIniter.TYPE_PROGRESS:
                        mTvProgress.setVisibility(View.VISIBLE);
                        mPb.setVisibility(View.INVISIBLE);
                        data.setOnInitProgressListener(this);
                        mTvProgress.setText((int)data.getCurFrequency() + "%");
                        break;
                }
                break;
            case AbsIniter.STATE_SUCCESS:
                mTvDesc.setTextColor(0xFF000000);
                mPb.setVisibility(View.INVISIBLE);
                mTvProgress.setVisibility(View.VISIBLE);
                mTvProgress.setText(" 成功 ");
                mTvResult.setTextColor(0xFF000000);
                mTvResult.setText("成功, 耗时: "+(data.getInitElapsedTime() / 1000)+"秒");
                break;
            case AbsIniter.STATE_FAIL:
                mTvDesc.setTextColor(0xFFff0000);
                mPb.setVisibility(View.INVISIBLE);
                mTvProgress.setVisibility(View.VISIBLE);
                // mTvProgress.setText(" fail ");
                mTvResult.setTextColor(0x88ff0000);
                mTvResult.setText("更新失败, 失败信息:\n "+data.getFailExtra().toString());
                break;
            case AbsIniter.STATE_EXCEPTION:
                mTvDesc.setTextColor(0xFFff0000);
                mPb.setVisibility(View.INVISIBLE);
                mTvProgress.setVisibility(View.VISIBLE);
                // mTvProgress.setText(" exception ");
                mTvResult.setTextColor(0xFFff0000);
                mTvResult.setText("更新异常, 错误信息:\n"+getThrowableMsg(data.getThrowableExtra()));
                break;
        }
    }

    private String getThrowableMsg(Throwable t){
        if(t == null) return "";
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        return writer.toString();
    }

    private final String FORMAT = "%d\\%";
    @Override
    public void onProgress(float total, float progress, float frequency) {
        mTvProgress.setText((int)frequency + "%");
    }
}
