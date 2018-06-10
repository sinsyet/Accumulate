package com.example.sample.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.sample.R;
import com.example.viewlib.utils.AppHelper;
import com.makeramen.roundedimageview.RoundedImageView;

public class MusicDial extends FrameLayout {

    private RoundedImageView riv;
    private ImageView[] ivs;
    private AnimatorSet[] asets;
    private LayoutParams params;

    public MusicDial(@NonNull Context context) {
        super(context);
        initView();
    }

    public MusicDial(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MusicDial(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicDial(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){
        riv = new RoundedImageView(getContext());
        riv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        riv.setCornerRadius((float) 10);
        riv.setBorderWidth((float) 2);
        riv.setBorderColor(Color.WHITE);
        riv.mutateBackground(true);
        riv.setImageResource(R.mipmap.img_portrait);
        riv.setBackground(new ColorDrawable(Color.GRAY));
        riv.setOval(true);
        // riv.setTileModeX(Shader.TileMode.REPEAT);
        // riv.setTileModeY(Shader.TileMode.REPEAT);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(riv,params);


        ObjectAnimator rotation = ObjectAnimator.ofFloat(riv, "rotation", 0, 360);
        rotation.setDuration(4000);
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setRepeatMode(ValueAnimator.RESTART);
        rotation.setInterpolator(new LinearInterpolator());


        mRotation = rotation;


    }

    private static final String TAG = "MusicDial";
    private void initMusicSymbolAnim(){
        ivs = new ImageView[3];
        asets = new AnimatorSet[3];
        params = new LayoutParams(
                AppHelper.dp2px(getContext(),30),
                AppHelper.dp2px(getContext(),30));
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = AppHelper.dp2px(getContext(),10);
            int rivWidth = getWidth();
            int rivHeight = getHeight();
        Log.e(TAG, "initMusicSymbolAnim: "+rivWidth+" // "+rivHeight);
        for (int i = 0; i < ivs.length; i++) {
            ivs[i] = new ImageView(getContext());
            ivs[i].setLayoutParams(params);
            switch (i % 3) {
                case 0:
                case 1:
                    ivs[i].setImageResource(R.mipmap.ic_music_note_2);
                    break;
                case 2:
                    ivs[i].setImageResource(R.mipmap.ic_music_note_1);
                    break;
            }
            // ivs[i].setTranslationX();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(ivs[i], "alpha", 0, 0.7f, 0.8f, 1.0f, 0.9f,0);
            alpha.setDuration(4200);
            alpha.setStartDelay(1400 * i);
            alpha.setRepeatMode(ValueAnimator.RESTART);
            alpha.setRepeatCount(ValueAnimator.INFINITE);
            ObjectAnimator translationX = ObjectAnimator.ofFloat(ivs[i], "translationX", 0, -rivWidth/2,-rivWidth);
            translationX.setDuration(4200);
            translationX.setStartDelay(1400 * i);
            translationX.setRepeatMode(ValueAnimator.RESTART);
            translationX.setRepeatCount(ValueAnimator.INFINITE);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(ivs[i], "translationY", rivHeight / 2,0, -rivHeight/2);
            translationY.setDuration(4200);
            translationY.setStartDelay(1400 * i);
            translationY.setRepeatMode(ValueAnimator.RESTART);
            translationY.setRepeatCount(ValueAnimator.INFINITE);
            AnimatorSet set = new AnimatorSet();
            // set.setDuration(4200);
            // set.setInterpolator(new LinearInterpolator());
            set.playTogether(alpha,translationX,translationY);
            asets[i] = set;
        }
    }

    private ObjectAnimator mRotation;

    public void startAnim(){
        mRotation.start();

        for (int i = 0; i < ivs.length; i++) {
            addView(ivs[i],params);
            asets[i].start();
        }


    }
    public void pauseAnim(){
        if (mRotation.isRunning()) {
            mRotation.pause();
            for (int i = 0; i < asets.length; i++) {
                asets[i].pause();
            }
        }
    }
    public void endAnim(){
        if (mRotation.isRunning()) {
            mRotation.end();
            for (int i = 0; i < asets.length; i++) {
                asets[i].end();
            }
        }
    }

    public void resumeAnim(){
        mRotation.resume();
        for (int i = 0; i < asets.length; i++) {
            asets[i].resume();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initMusicSymbolAnim();
    }
}
