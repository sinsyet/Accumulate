package com.example.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.sample.async.initer.AbsIniter;
import com.example.sample.holders.InitHolder;

import java.util.List;

public class InitAdapter extends RecyclerView.Adapter {

    private final List<Object> items;

    private static final int INITER = 1;
    private static final int INITER_QUEUE = 2;

    public InitAdapter(List<Object> items){
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case INITER:
                // holder = new InitHolder(View.inflate())
                break;
            default:

                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = items.get(position);
        if(o instanceof AbsIniter){
            return INITER;
        }
        // return super.getItemViewType(position);
        return INITER_QUEUE;
    }
}
