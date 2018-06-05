package com.example.sample.async.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sample.async.initer.AbsIniter;
import com.example.sample.holders.InitHolder;

import java.util.List;

public class InitAdapter extends BaseAdapter {

    private final List<AbsIniter> items;

    public InitAdapter(List<AbsIniter> items){
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
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
        InitHolder holder;
        if(convertView == null){
            holder = new InitHolder(parent.getContext());
        }else{
            holder = (InitHolder) convertView.getTag();
        }
        holder.bindData(items.get(position));

        return holder.getView();
    }
}
