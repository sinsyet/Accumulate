package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.sample.R;
import com.example.viewlib.drawables.GraphDrawable;

public class GraphActivity extends AppCompatActivity {

    private ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        view = (ImageView) findViewById(R.id.graph_iv);
        view.setImageDrawable(new GraphDrawable(getApplicationContext()));
    }
}
