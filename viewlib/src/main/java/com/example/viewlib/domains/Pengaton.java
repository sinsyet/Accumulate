package com.example.viewlib.domains;


import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

public class Pengaton {
    private static final String TAG = "Pengaton";
    private static final float ANGLE = 36;
    private float mCx;
    private float mCy;
    private float mRadius;

    public Path mPath = new Path();

    public PointF mCenter = new PointF();

    private PointF[] mPoints;

    public Pengaton(float cx, float cy, float radius) {
        this.mCx = cx;
        this.mCy = cy;
        this.mRadius = radius;


        // calcPath();
        calcPentagramPath();
    }

    private void calcPentagonPath() {
        float radius = this.mRadius;
        float cx = this.mCx;
        float cy = this.mCy;

        final int LEN = 5;
        mPoints = new PointF[LEN];
        for (int i = 0; i < LEN; i++) {
            mPoints[i] = new PointF();
        }

        float dx1, dy1, dx2, dy2;

        dx1 = (float) (Math.cos(angle2Arc(ANGLE)) * radius);
        dy1 = (float) (Math.sin(angle2Arc(ANGLE)) * radius);

        dx2 = (float) ((Math.pow(2 * dx1, 2) - 2 * Math.pow(radius, 2)) / (2 * radius));
        dy2 = (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(dx2, 2));

        mPoints[0].set(cx + dx1, cy);
        mPoints[1].set(cx + dx1 * 2, cy + dy1);
        mPoints[2].set(cx + dx2 + radius, cy + dy1 + dy2);
        mPoints[3].set(cx + dx2, cy + dy1 + dy2);
        mPoints[4].set(cx, cy + dy1);

        mPath.reset();
        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        for (int i = 1; i < 5; i++) {
            mPath.lineTo(mPoints[i].x, mPoints[i].y);
        }
    }

    public static float angle2Arc(float angle) {
        return (float) ((Math.PI / 180) * angle);
    }

    private void calcPentagramPath() {
        float radius = this.mRadius;
        float cx = this.mCx;
        float cy = this.mCy;

        final int LEN = 10;
        mPoints = new PointF[LEN];
        for (int i = 0; i < LEN; i++) {
            mPoints[i] = new PointF();
        }

        float dx1, dy1, dx2, dy2;

        dx1 = (float) (Math.cos(angle2Arc(ANGLE)) * radius);
        dy1 = (float) (Math.sin(angle2Arc(ANGLE)) * radius);

        dx2 = (float) ((Math.pow(2 * dx1, 2) - 2 * Math.pow(radius, 2)) / (2 * radius));
        dy2 = (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(dx2, 2));

        float dx3, dy3, dx4, dy4, dx5, dy5;

        dy3 = dy1;
        dx3 = (float) ((Math.pow(dy3, 2) + Math.pow(dx1, 2)) / (2 * dx1));

        // star__line_len, 五角星的边长
        float star_line_len = (float) Math.sqrt(Math.pow(dx1 - dx3, 2) + Math.pow(dy3, 2));

        double acos1 = Math.acos(dy3 / star_line_len);
        dx4 = (float) (Math.cos(acos1 * 2) * star_line_len);
        dy4 = (float) (Math.sin(acos1 * 2) * star_line_len);

        dx5 = dx1;
        dy5 = (dx5 * dy4) / dx4;
        // dx4 =
        mPoints[0].set(cx + dx1, cy);
        mPoints[1].set(cx + dx1 + (dx1 - dx3), cy + dy1);
        mPoints[2].set(cx + dx1 * 2, cy + dy1);
        mPoints[3].set(cx + dx1 + (dx1 - dx4), cy + dy1 + dy4);
        mPoints[4].set(cx + dx2 + radius, cy + dy1 + dy2);
        mPoints[5].set(cx + dx1, cy + dy1 + dy5);
        mPoints[6].set(cx + dx2, cy + dy1 + dy2);
        mPoints[7].set(cx + dx4, cy + dy1 + dy4);
        mPoints[8].set(cx, cy + dy1);
        mPoints[9].set(cx + dx3, cy + dy3);

        // 中心点
        mCenter.set(cx + dx1 - dx1, cy + (dy1 + dy2) / 2 - (dy1 + dy2) / 2);


        mPath.reset();
        mPoints[0].x -=  dx1;
        mPoints[0].y -=  (dy1 + dy2) / 2;
        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        for (int i = 1; i < 10; i++) {
            mPoints[i].x -=  dx1;
            mPoints[i].y -=  (dy1 + dy2) / 2;
            mPath.lineTo(mPoints[i].x, mPoints[i].y);
        }
        Log.e(TAG, "calcPentagramPath: ");
    }
}
