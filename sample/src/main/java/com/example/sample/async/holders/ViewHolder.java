package com.example.sample.async.holders;

import android.content.Context;
import android.view.View;

public abstract class ViewHolder<Data> {

    private final View root;

    public ViewHolder(Context ctx){
        root = onCreateView(ctx);
        root.setTag(this);
        onViewCreated(root);
    }

    protected abstract View onCreateView(Context ctx);
    protected abstract void onViewCreated(View view);

    public View getView() {
        return root;
    }

    public abstract void bindData(Data data);
}
