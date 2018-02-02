package com.example.sample.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sample.holders.SampleAtyHolder;

import java.util.List;


public class SampleAtyAdapter extends BaseAdapter {
    private List<Class<? extends Activity>> mAtys;

    public SampleAtyAdapter(List<Class<? extends Activity>> mAtys){
        this.mAtys = mAtys;
    }
    @Override
    public int getCount() {
        return mAtys.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SampleAtyHolder holder;
        if (convertView == null) {
            holder = new SampleAtyHolder(parent.getContext());
        }else{
            holder = (SampleAtyHolder) convertView.getTag();
        }
        holder.bindData(mAtys.get(position));

        return holder.getView();
    }
}
