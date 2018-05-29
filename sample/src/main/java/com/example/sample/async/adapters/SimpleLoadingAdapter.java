package com.example.sample.async.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sample.async.holders.SimpleLoadingHolder;
import com.example.sample.async.initer.AbsIniter;

import java.util.List;

public class SimpleLoadingAdapter extends BaseAdapter {

    private final List<AbsIniter> initers;

    public SimpleLoadingAdapter(List<AbsIniter> initers){
        this.initers = initers;
    }


    @Override
    public int getCount() {
        return initers.size();
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
        SimpleLoadingHolder holder = null;
        if(convertView == null){
            holder = new SimpleLoadingHolder(parent.getContext());
        }else{
            holder = (SimpleLoadingHolder) convertView.getTag();
        }
        holder.bindData(initers.get(position));
        return holder.getView();
    }
}
