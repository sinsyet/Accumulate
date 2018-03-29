package com.example.sample.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.sample.R;
import com.example.sample.views.PentacleView;

public class ViewActivity extends AppCompatActivity {

    private PentacleView mView;

    private Handler mHandler = new Handler();
    private View mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mView = findViewById(R.id.view_pv);
        mIv = findViewById(R.id.view_iv);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAnim();
            }
        },2 * 1000);
    }

    private static final String TAG = "ViewActivity";
    private void playAnim(){
        // 貌似只对ImageView的src属性有效
        Log.e(TAG, "playAnim: ");
        RotateAnimation ra = new RotateAnimation(
                0,
                360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5F);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatMode(Animation.RESTART);
        ra.setDuration(2 * 1000);
        ra.setRepeatCount(Animation.INFINITE);
//        mIv.setAnimation(ra);
        mView.startAnimation(ra);
//        ra.start();
    }
    private void playAnim2(){
        // 貌似只对ImageView的src属性有效
        Log.e(TAG, "playAnim: ");

        ObjectAnimator ra = ObjectAnimator.ofFloat(mView, "rotationY", 0, 360);

        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatMode(ValueAnimator.RESTART);
        ra.setDuration(2 * 1000);
        ra.setRepeatCount(ValueAnimator.INFINITE);
        ra.start();
    }
}
