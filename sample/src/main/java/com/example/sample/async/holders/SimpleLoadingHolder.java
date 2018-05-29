package com.example.sample.async.holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.sample.R;
import com.example.sample.async.initer.AbsIniter;


public class SimpleLoadingHolder extends ViewHolder<AbsIniter> {

    private TextView mTv;

    public SimpleLoadingHolder(Context ctx) {
        super(ctx);
    }

    @Override
    protected View onCreateView(Context ctx) {
        return View.inflate(ctx, R.layout.item_initer,null);
    }

    @Override
    protected void onViewCreated(View view) {
        mTv = view.findViewById(R.id.initer_tv);
    }

    @Override
    public void bindData(AbsIniter absIniter) {
        mTv.setText((String)absIniter.getHintExtra());
    }
}
