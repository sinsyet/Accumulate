package com.example.sample.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sample.activities.FragmentWrapperActivity;

public class FragmentRecyHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "FragmentRecyHolder";
    private TextView mTv;

    public FragmentRecyHolder(View itemView) {
        super(itemView);
        onHolderViewCreated(itemView);
    }

    void onHolderViewCreated(View view){
        mTv = (TextView) view;

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();

            try {
                @SuppressWarnings("unchecked")
                Class<? extends Fragment> clazz = (Class<? extends Fragment>) tag;
                Context aty = v.getContext();
                Intent intent = new Intent(aty, FragmentWrapperActivity.class);
                intent.putExtra("name", clazz);
                aty.startActivity(intent);
            }catch (ClassCastException e){
                Log.d(TAG, "onClick, invalid tag");
            }
        }
    };

    public void bindData(Class<? extends Fragment> s){
        mTv.setText(s.getSimpleName());
        mTv.setOnClickListener(mClickListener);
        mTv.setTag(s);
    }
}
