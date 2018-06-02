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

import com.example.sample.R;

import java.util.ArrayList;
import java.util.List;

public class InitDialog extends Dialog implements View.OnClickListener {

    private RecyclerView recyv;

    private List<Object> items = new ArrayList<>();

    public InitDialog(@NonNull Context context) {
        super(context);

        findView();
    }

    void findView(){
        initDialogSize();
        View v = View.inflate(getContext(), R.layout.dialog_init, null);

        recyv = v.findViewById(R.id.init_recyv);
        configAdapter();
        v.findViewById(R.id.init_cancle).setOnClickListener(this);
        setContentView(v);
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

    private void configAdapter(){
        recyv.setLayoutManager(
                new StaggeredGridLayoutManager(
                        1,
                        StaggeredGridLayoutManager.VERTICAL));


    }

    @Override
    public void onClick(View v) {

    }
}
