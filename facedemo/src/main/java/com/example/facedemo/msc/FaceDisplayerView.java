package com.example.facedemo.msc;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FaceDisplayerView extends FrameLayout {

    private LayoutParams mParams;
    private IFaceDrawer mFaceDrawer;

    public FaceDisplayerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public FaceDisplayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FaceDisplayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FaceDisplayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        addView(getDisplayer(),0,mParams);
        mFaceDrawer = getFaceDrawer();
        addView(mFaceDrawer.getView(),1,mParams);
    }

    protected View getDisplayer(){
        return new TextureView(getContext());
    }

    protected IFaceDrawer getFaceDrawer(){
        return new FaceDrawerWrapper(getContext());
    }
}
