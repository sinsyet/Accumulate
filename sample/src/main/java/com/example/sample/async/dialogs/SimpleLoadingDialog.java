package com.example.sample.async.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.example.sample.R;
import com.example.sample.async.adapters.SimpleLoadingAdapter;
import com.example.sample.async.initer.AbsIniter;

import java.util.List;

public class SimpleLoadingDialog extends BaseDialog implements View.OnClickListener {

    private final List<AbsIniter> initers;
    private final Callback cb;
    private ListView lv;
    private Button btn;
    private SimpleLoadingAdapter adapter;

    public SimpleLoadingDialog(@NonNull Context context, List<AbsIniter> initers, Callback cb) {
        super(context);
        this.initers = initers;
        this.cb = cb;
        initView();
    }


    void initView(){
        View v = View.inflate(getContext(), R.layout.dialog_simple_loading,null);
        findView(v);
        setContentView(v);
    }

    void findView(View v){
        lv = v.findViewById(R.id.simpleLoading_lv);
        btn = v.findViewById(R.id.simpleLoading_btn);

        btn.setOnClickListener(this);
        adapter = new SimpleLoadingAdapter(initers);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        dismiss();
        if (cb != null) {
            cb.onCancle();
        }
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public interface Callback{
        void onCancle();
    }
}
