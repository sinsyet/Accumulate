package com.example.viewlib.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.apphelper.AppHelper;
import com.example.viewlib.R;


public class RxDialog extends Dialog {
    private static final String TAG = "RxDialog";
    protected Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    protected LayoutParams mLayoutParams;

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public RxDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);

    }

    public RxDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public RxDialog(Context context) {
        super(context);
        initView(context);
    }

    private void getScreenSize(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
    }
    public void setWrapScreenHeightSize() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams lp = window.getAttributes();
        lp.height = LayoutParams.WRAP_CONTENT;

        lp.width = mScreenWidth - 2 * AppHelper.dp2px(getContext(),40);
        window.setAttributes(lp);
    }

    private void initView(Context context) {
        getScreenSize();
        // requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉黑色头部
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);//只有这样才能去掉黑色背景
        // 这行代码必须添加; 给对话框添加一个透明背景布局; 不然dialog布局显示好像会很挤
        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);

        mContext = context;
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;         // 透明度, 0表示全透明; 1表示全不透明
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = Gravity.CENTER;
        }
    }

    /**
     * @param context
     * @param alpha   透明度 0.0f--1f(不透明)
     * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
     */
    public RxDialog(Context context, float alpha, int gravity) {
        super(context);
        // TODO Auto-generated constructor stub
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        mContext = context;
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = gravity;
        }
    }

    /**
     * 隐藏头部导航栏状态栏
     */
    public void skipTools() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置全屏显示
     */
    public void setFullScreen() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.FILL_PARENT;
        lp.height = LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
    }

    /**
     * 设置宽度match_parent
     */
    public void setFullScreenWidth() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.FILL_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    /**
     * 设置高度为match_parent
     */
    public void setFullScreenHeight() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
    }

    public void setOnWhole() {
        getWindow().setType(LayoutParams.TYPE_SYSTEM_ALERT);
    }
}
