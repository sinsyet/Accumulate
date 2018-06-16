package com.example.sample.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sample.holders.FragmentRecyHolder;

import java.util.List;

public class FragmentRecyAdapter extends RecyclerView.Adapter<FragmentRecyHolder> {

    private final List<Class<? extends Fragment>> frags;

    public FragmentRecyAdapter(List<Class<? extends Fragment>> frags){
        this.frags = frags;
    }

    @Override
    public FragmentRecyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new FragmentRecyHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(FragmentRecyHolder holder, int position) {
        holder.bindData(frags.get(position));
    }

    @Override
    public int getItemCount() {
        return frags.size();
    }
}
