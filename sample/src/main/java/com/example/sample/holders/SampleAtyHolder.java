package com.example.sample.holders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.sample.R;


public class SampleAtyHolder extends ViewHoder<Class<? extends Activity>> {

    private TextView mTv;

    public SampleAtyHolder(Context ctx) {
        super(ctx);
    }

    @Override
    protected View onCreateView() {

        return View.inflate(getContext(), R.layout.item_sampleatys,null);
    }

    @Override
    protected void onViewCreated(View v) {
        mTv = (TextView) v.findViewById(R.id.sampleatys_tv);

    }

    @Override
    public void bindData(Class<? extends Activity> data) {
        mTv.setText(data.getSimpleName());
    }
}
