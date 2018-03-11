package com.example.sample.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.sample.R;
import com.example.viewlib.drawables.CallingDrawable;
import com.example.viewlib.drawables.Graph2Drawable;
import com.example.viewlib.drawables.GraphDrawable;

public class GraphActivity extends AppCompatActivity {

    private ImageView view;
    private Graph2Drawable callingDrawable;
    private CallingDrawable callingDrawable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        view = (ImageView) findViewById(R.id.graph_iv);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               /* callingDrawable = new Graph2Drawable(getApplicationContext());
                view.setImageDrawable(callingDrawable);
                //callingDrawable.onViewSizeEffect(view.getWidth(),view.getHeight());
                //callingDrawable.start();*/
                callingDrawable1 = new CallingDrawable(getApplicationContext());
                view.setImageDrawable(callingDrawable1);
                callingDrawable1.onViewSizeEffect(view.getWidth(),view.getHeight());
                callingDrawable1.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        callingDrawable.stop();
    }
}
