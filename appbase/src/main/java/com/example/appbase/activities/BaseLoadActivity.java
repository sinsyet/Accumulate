package com.example.appbase.activities;


import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewlib.layout.SimpleLoadLayout;


public class BaseLoadActivity extends BaseActivity implements SimpleLoadLayout.OnLoadActionListener {

    private SimpleLoadLayout mLoadLayout;

    private ViewGroup.LayoutParams mMatchWidthHeightParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mLoadLayout = new SimpleLoadLayout(this);
        View successView = View.inflate(this, layoutResID, null);
        mLoadLayout.setLoadSuccessView(successView);
        mLoadLayout.setOnLoadActionListener(this);
        super.setContentView(mLoadLayout,mMatchWidthHeightParams);
    }

    protected void onLoading(){
        mLoadLayout.onLoading();
    }

    protected void onLoadEmpty(){
        mLoadLayout.onLoadEmpty();
    }

    protected void onLoadException(){
        mLoadLayout.onLoadFail();
    }

    protected void onLoadFail(){
        mLoadLayout.onServerError();
    }

    protected void onLoadSuccess(){
        mLoadLayout.onLoadSuccess();
    }

    @Override
    public void onReLoadClick(int reloadType) {

    }

    @Override
    public void onCheckNet() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
