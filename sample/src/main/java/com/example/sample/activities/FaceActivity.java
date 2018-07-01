package com.example.sample.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.apphelper.AppHelper;
import com.example.sample.R;


public class FaceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIv;
    private Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_face);

        findView();
    }

    private void findView() {
        mIv = findViewById(R.id.face_iv);
        findViewById(R.id.face_load).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_load : load(); break;
        }
    }

    int REQUEST_CODE_SELECT_PIC = 10;
    private void load() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_PIC && resultCode == Activity.RESULT_OK){
            //获取选择的图片
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String selectedImagePath = cursor.getString(columnIndex);
            bm = BitmapFactory.decodeFile(selectedImagePath);
            //要使用Android内置的人脸识别，需要将Bitmap对象转为RGB_565格式，否则无法识别
            bm = bm.copy(Bitmap.Config.RGB_565, true);
            cursor.close();
            mIv.setImageBitmap(bm);

            AppHelper.run(findFace);
        }
    }

    private final int MAX_FACE_NUM = 10;
    private Runnable findFace = new Runnable() {
        @Override
        public void run() {
            if(bm == null) return;

            FaceDetector faceDetector = new FaceDetector(bm.getWidth(), bm.getHeight(), MAX_FACE_NUM);
            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_NUM];
             PointF pointF = new PointF();

            long start = System.currentTimeMillis();
            int count = faceDetector.findFaces(bm, faces);
            Log.e(TAG, "run: time: "+(System.currentTimeMillis()-start));
            final Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(bitmap,0,0,null);
            canvas.drawBitmap(bm,0,0,null);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20);

            for (int i = 0; i < count; i++) {
                FaceDetector.Face face = faces[i];
                face.getMidPoint(pointF);
                int v = (int) face.eyesDistance();

                Rect rect = new Rect();
                rect.set(
                        (int)(pointF.x - v * 0.8),
                        (int)(pointF.y - v * 0.7),
                        (int)(pointF.x + v * 0.8),
                        (int)(pointF.y + v * 1.2));
                canvas.drawRect(rect,paint);
                Log.e(TAG, "run: "+face.confidence()+" // "+face.eyesDistance()+" // "+pointF.toString());
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIv.setImageBitmap(bitmap);
                }
            });
            Log.e(TAG, "run: "+count);
        }
    };
    private static final String TAG = "FaceActivity";


}
