package com.example.sample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appbase.async.AsyncEventSession;
import com.example.sample.R;
import com.example.sample.async.adapters.InitAdapter;
import com.example.sample.async.initer.AbsIniter;

import java.util.List;

public class InitDialog extends Dialog implements View.OnClickListener {

    private List<AbsIniter> items;
    private ListView lv;
    private InitAdapter adapter;
    private ProgressBar pb;
    private TextView tv;
    private View ll;
    private Callback cb;


    public InitDialog(@NonNull Context context, List<AbsIniter> items) {
        super(context);
        this.items = items;
        findView();
    }



    void findView(){
        initDialogSize();
        View v = View.inflate(getContext(), R.layout.dialog_init, null);

        lv = v.findViewById(R.id.init_lv);
        pb = v.findViewById(R.id.init_pb);
        tv = v.findViewById(R.id.init_tv_desc);
        ll = v.findViewById(R.id.init_ll);

        v.findViewById(R.id.init_btn_interrupt).setOnClickListener(this);
        v.findViewById(R.id.init_btn_cancle2).setOnClickListener(this);
        v.findViewById(R.id.init_btn_enter).setOnClickListener(this);
        setContentView(v);

        setAdapter();
    }

    private void initDialogSize() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕的0.8
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度设置为屏幕的0.8
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
        switch (v.getId()) {
            case R.id.init_btn_interrupt:
                if(mInitStatus == AsyncEventSession.Status.RUNNING) {
                    if (cb != null) {
                        cb.onAction(Callback.OP_INTERRUPT);
                    }
                }else if(mInitStatus == AsyncEventSession.Status.INTERRUPT
                        || mInitStatus == AsyncEventSession.Status.FAIL){
                    dismiss();
                }
                break;
            case R.id.init_btn_cancle2:
                break;
            case R.id.init_btn_enter:
                break;
        }
    }

    public void onInterrupt(){
        tv.setText("正在取消初始化, 请稍后...");
        mInitStatus = AsyncEventSession.Status.INTERRUPT;
    }

    public void updateProgress(int cur, int total){
        int progress = (int) (cur * 100f / total);
        pb.setProgress(progress);
        tv.setText("正在初始化中..."+(progress) + "%("+cur+"/"+total+")");
    }

    private int mInitStatus = AsyncEventSession.Status.RUNNING;
    public void onInitFail(){
        tv.setText("初始化失败");
        mInitStatus = AsyncEventSession.Status.FAIL;
    }

    public void onInitSuccess(int sucessCount, int failCount, int exceptionCount){
        tv.setText("初始化结束 (成功"+sucessCount+"个, 失败:"+failCount+"个, 异常:"+exceptionCount+"个)");
        mInitStatus = AsyncEventSession.Status.SUCCESS;
    }

    public void notifyDataSetChange(){
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setCallback(Callback cb){
        this.cb = cb;
    }
    public interface Callback{
        int OP_INTERRUPT = 0;
        void onAction(int op);
    }
}
