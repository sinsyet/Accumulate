package com.example.sample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.sample.R;
import com.example.sample.async.adapters.InitAdapter;
import com.example.sample.async.initer.AbsIniter;

import java.util.ArrayList;
import java.util.List;

public class InitDialog extends Dialog implements View.OnClickListener {

    private List<AbsIniter> items;
    private ListView lv;
    private InitAdapter adapter;


    public InitDialog(@NonNull Context context, List<AbsIniter> items) {
        super(context);
        this.items = items;
        findView();
    }



    void findView(){
        initDialogSize();
        View v = View.inflate(getContext(), R.layout.dialog_init, null);

        lv = v.findViewById(R.id.init_lv);

        v.findViewById(R.id.init_btn_cancle).setOnClickListener(this);
        setContentView(v);

        setAdapter();
    }

    private void initDialogSize() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        lp.height = (int) (d.heightPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        lp.gravity = Gravity.CENTER;
        lp.alpha = 1f;
        dialogWindow.setAttributes(lp);
    }

    private void setAdapter(){
        adapter = new InitAdapter(items);
        lv.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {

    }

    public void notifyDataSetChange(){
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
