package com.example.sample.holders;

import android.content.Context;
import android.view.View;


public abstract class ViewHolder<T> {

    private Context mCtx;
    protected View root;

    public ViewHolder(Context ctx){
        this.mCtx = ctx;

        root = onCreateView();
        if(root != null){
            root.setTag(this);
        }
        onViewCreated(root);
    }

    protected abstract View onCreateView();

    protected void onViewCreated(View v){}

    protected Context getContext(){
        return mCtx;
    }

    public View getView(){
        return root;
    }

    public abstract void bindData(T data);
}
