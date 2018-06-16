package com.example.sample.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sample.R;

import java.io.Serializable;

public class FragmentWrapperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_wrapper);

        injectFragment();
    }

    private void injectFragment() {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Fragment> clazz
                    = (Class<? extends Fragment>) getIntent().getSerializableExtra("name");
            Fragment fragment = clazz.newInstance();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_wrapper, fragment);
            ft.commit();
        } catch (Exception e) {
            TextView tv = new TextView(this);
            FrameLayout atyWrapper = findViewById(R.id.fragment_wrapper);
            tv.setText(e.getMessage());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            atyWrapper.addView(tv,params);
        }
    }
}
