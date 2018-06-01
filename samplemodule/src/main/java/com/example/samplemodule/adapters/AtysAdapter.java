package com.example.samplemodule.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.samplemodule.R;
import com.example.samplemodule.holders.AtysHolder;

import java.util.List;

public class AtysAdapter extends RecyclerView.Adapter<AtysHolder> {
    private static final String TAG = "AtysAdapter";
    private final List<Class<? extends Activity>> atys;

    public AtysAdapter(List<Class<? extends Activity>> atys){
        this.atys = atys;
        Log.e(TAG, "AtysAdapter: "+atys.size());
    }

    @NonNull
    @Override
    public AtysHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AtysHolder(View.inflate(parent.getContext(), R.layout.item_atys,null));
    }

    @Override
    public void onBindViewHolder(@NonNull AtysHolder holder, int position) {
        holder.bind(atys.get(position));
        Log.e(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: ");
        return atys.size();
    }
}
