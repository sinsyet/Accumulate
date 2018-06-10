package com.example.sample.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.sample.R;
import com.example.sample.views.MusicDial;
import com.example.viewlib.utils.AppHelper;

/**
 * 模仿抖音视频播放页面
 *
 * 要点:
 *  1. 状态栏透明, 标题栏去掉
 *  2. 视频可切换;
 */
public class DouYinActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView mVideo;
    private ImageView mIvArrow;
    private View mPlayer;
    private RadioGroup mRadioGroup;
    private int mCheckedRadioButtonId;
    private MusicDial mMusicDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_yin);

        findView();
    }

    private void findView() {
        mVideo = findViewById(R.id.douyin_video);
        mIvArrow = findViewById(R.id.douyin_iv_arrow);
        mPlayer = findViewById(R.id.douyin_fl_player);
        mMusicDial = findViewById(R.id.douyin_musicDial);

        mRadioGroup = findViewById(R.id.douyin_rg);

        mPlayer.setOnClickListener(this);
        // videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ videos[position%2]));
        mVideo.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.video_1));

        mCheckedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(mCheckedRadioButtonId);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextPaint paint = view.getPaint();
                paint.setFakeBoldText(false);

                view = group.findViewById(checkedId);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                paint = view.getPaint();
                paint.setFakeBoldText(true);


                mCheckedRadioButtonId = checkedId;

            }
        });
        playVideo();
    }

    private void playVideo(){
        mVideo.start();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mIvArrow.animate().alpha(0).setDuration(200).start();
                int duration = mVideo.getDuration();
                Log.e(TAG, "onPrepared: "+duration);
                mMusicDial.startAnim();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(mVideo.isPlaying()){
            mVideo.pause();
            mIvArrow.animate().alpha(1.0f).setDuration(200).start();
            int currentPosition = mVideo.getCurrentPosition();
            Log.e(TAG, "onClick: "+currentPosition);
            mMusicDial.pauseAnim();
        }else{
            mVideo.start();
            int currentPosition = mVideo.getCurrentPosition();
            mIvArrow.animate().alpha(0).setDuration(200).start();
            Log.e(TAG, "onClick: "+currentPosition);
            mMusicDial.resumeAnim();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicDial.endAnim();
    }

    private static final String TAG = "DouYinActivity";
}
