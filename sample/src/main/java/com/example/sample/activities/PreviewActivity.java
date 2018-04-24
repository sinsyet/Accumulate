package com.example.sample.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sample.R;

public class PreviewActivity extends AppCompatActivity {
    public static final int PHOTO = 1;
    public static final int TEXT = 2;
    public static final int VIDEO = 3;
    private ViewGroup mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_preview);
        mPreview = findViewById(R.id.preview_cl);
        handlePreview();
    }

    private void handlePreview() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        String path = intent.getStringExtra("path");
        switch (type) {
            case PHOTO:
                showPhoto(path);
                break;
            case TEXT:

                break;
            case VIDEO:

                break;
        }
    }

    private void showPhoto(String path){
        ImageView iv = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bitmap);
        mPreview.addView(iv,params);
    }

    private void setFullScreen() {
          /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
