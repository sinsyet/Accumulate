package com.example.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

public class RippleActivity extends AppCompatActivity {

    private RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        rippleBackground = (RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        rippleBackground.stopRippleAnimation();
    }
}
