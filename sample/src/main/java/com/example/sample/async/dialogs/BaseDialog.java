package com.example.sample.async.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
        initDialogSize();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialogSize();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialogSize();
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


}
