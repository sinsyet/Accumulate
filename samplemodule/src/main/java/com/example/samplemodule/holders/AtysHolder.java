package com.example.samplemodule.holders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.samplemodule.R;

public class AtysHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "AtysHolder";
    private final View itemView;
    private TextView mTv;

    public AtysHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        itemView.setOnClickListener(mClickListener);
        findView(itemView);
    }

    void findView(View v){
        mTv = v.findViewById(R.id.atys_tv);
    }

    public void bind(Class<? extends Activity> clz){
        Log.e(TAG, "bind: "+clz.getSimpleName());
        mTv.setText(clz.getSimpleName().replace("Activity",""));
        itemView.setTag(clz);
    }

    final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Class<? extends Activity> atyClz = (Class<? extends Activity>) v.getTag();

                Context ctx = v.getContext();
                ctx.startActivity(new Intent(ctx,atyClz));
            }catch (ClassCastException ignored){
            }

        }
    };
}
