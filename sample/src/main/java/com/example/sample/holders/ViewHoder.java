package com.example.sample.holders;

import android.content.Context;
import android.view.View;


public abstract class ViewHoder<T> {

    private Context mCtx;
    protected View root;

    public ViewHoder(Context ctx){
        this.mCtx = ctx;

        root = onCreateView();
        if(root != null){
            root.setTag(this);
        }
        onViewCreated(root);
    }

    protected View onCreateView(){
        return null;
    }

    protected void onViewCreated(View v){}

    protected Context getContext(){
        return mCtx;
    }

    public View getView(){
        return root;
    }

    public abstract void bindData(T data);
}
