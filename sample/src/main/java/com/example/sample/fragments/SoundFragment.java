package com.example.sample.fragments;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sample.R;

public class SoundFragment extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    private int beep;
    private int sample_audio;
    private SoundPool soundPool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sound, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sound_btn_play).setOnClickListener(this);

        initSoundPool();
    }

    void initSoundPool(){
        SoundPool sPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        beep = sPool.load(getContext(), R.raw.beep, 1);
        sample_audio = sPool.load(getContext(), R.raw.sample_audio, 2);
        /*sPool.load(getContext(),R.raw.star, 3);
        sPool.load(getContext(),R.raw.smile, 3);*/
        sPool.setOnLoadCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        if (soundPool != null) {
            // soundPool.play(beep,0.5f,0.5f, 1, 0, 1);
            soundPool.play(sample_audio,0.5f,0.5f, 1, 0, 1);
        }else{
            Log.e(TAG, "onClick: soundPool is null ");
        }
    }

    private static final String TAG = "SoundFragment";
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        // int soundID, float leftVolume, float rightVolume,
        //            int priority, int loop, float rate
        this.soundPool = soundPool;
    }
}
