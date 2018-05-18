package com.example.facedemo.msc;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 脸轮廓
 *
 * @author YGX
 */
public class FaceR {
    private int count;
    private List<Face> faces = new ArrayList<>();

    public static class Face{

        private Path position = new Path();
        private List<Path> landmarks = new ArrayList<>();
        private List<PointF> points = new ArrayList<>();

        public List<Path> getLandmarks() {
            return landmarks;
        }

        public Path getPosition() {
            return position;
        }

        public List<PointF> getPoints() {
            return points;
        }
    }

    public int getCount() {
        return count;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public static FaceR decode(String s, float rate, int width, int height, int facing){
        if(TextUtils.isEmpty(s)) return null;

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(s);
            int ret = jsonObj.getInt("ret");
            if(ret != 0) return null;

            JSONArray face = jsonObj.getJSONArray("face");
            if(face == null) return null;


            FaceR r = new FaceR();
            final int len = face.length();
            r.count = len;
            int bottom = 0,
                right  = 0,
                left   = 0,
                top    = 0;

            Rect rect = null;
            for (int i = 0; i < len; i++) {
                JSONObject faceJSONObject = face.getJSONObject(i);

                JSONObject position = faceJSONObject.getJSONObject("position");
                bottom = position.getInt("bottom");
                right  = position.getInt("right" );
                left   = position.getInt("left"  );
                top    = position.getInt("top"   );

                rect = new Rect(left, top, right, bottom);
                rect.bottom = transY(rect.bottom, rate,height, facing);
                rect.right  = trans(rect.right , rate,width ,facing);
                rect.left   = trans(rect.left  , rate,width ,facing);
                rect.top    = transY(rect.top   , rate,height,facing);
                Face f = new Face();
                Path p = f.position;
                p.moveTo(rect.left , rect.top   );
                p.lineTo(rect.right, rect.top   );
                p.lineTo(rect.right, rect.bottom);
                p.lineTo(rect.left , rect.bottom);
                p.lineTo(rect.left , rect.top   );
                p.close();

                // 获取界标信息
                JSONObject landmark = faceJSONObject.getJSONObject("landmark");

                // 左眉, 有三个关键点
                Path left_eyebrow_path = get_3pointer_path_middle(landmark,"left_eyebrow", rate, width, height, f.points, facing);
                f.landmarks.add(left_eyebrow_path);

                // 右眉, 有三个关键点
                Path right_eyebrow_path = get_3pointer_path_middle(landmark,"right_eyebrow", rate, width, height, f.points, facing);
                f.landmarks.add(right_eyebrow_path);

                // 右眼, 有两个关键点
                Path right_eye_path = get_3pointer_path_center(landmark,"right_eye", rate, width, height, f.points, facing);
                f.landmarks.add(right_eye_path);

                // 左眼, 有两个关键点
                Path left_eye_path = get_3pointer_path_center(landmark,"left_eye", rate, width, height, f.points, facing);
                f.landmarks.add(left_eye_path);

                // 鼻子
                Path nose_path = get_nose_path(landmark, rate, width, height,f.points, facing);
                f.landmarks.add(nose_path);

                // 嘴巴, 有5个关键点(左嘴角坐标, 右嘴角坐标, 上嘴唇坐标, 下嘴唇坐标, 嘴中间)
                Path mouse_path = get_mouse_path(landmark, rate, width, height,f.points, facing);
                f.landmarks.add(mouse_path);

                r.faces.add(f);
            }
            return r;
        } catch (JSONException e) {

        }
        return null;
    }

    private static Path get_nose_path(JSONObject landmark, float rate, int width, int height, List<PointF> points, int facing) throws JSONException {
        JSONObject nose_top     = landmark.getJSONObject("nose_top"   );
        JSONObject nose_right   = landmark.getJSONObject("nose_right" );
        JSONObject nose_bottom  = landmark.getJSONObject("nose_bottom");
        JSONObject nose_left    = landmark.getJSONObject("nose_left"  );
        Path path = new Path();
        path.reset();
        int top_x = trans(nose_top.getInt("x"), rate, width, facing);
        int top_y = transY(nose_top.getInt("y"), rate, height,facing);
        path.moveTo(top_x, top_y);
        int right_x = trans(nose_right.getInt("x"), rate, width,facing);
        int right_y = transY(nose_right .getInt("y"), rate, height,facing);
        path.lineTo(right_x, right_y);
        int bottom_x = trans(nose_bottom.getInt("x"), rate, width,facing);
        int bottom_y = transY(nose_bottom.getInt("y"), rate, height,facing);
        path.lineTo(bottom_x, bottom_y);
        int left_x = trans(nose_left.getInt("x"), rate, width,facing);
        int left_y = transY(nose_left.getInt("y"), rate, height,facing);
        path.lineTo(left_x, left_y);
        path.lineTo(top_x, top_y);

        points.add(new PointF(top_x,top_y));
        points.add(new PointF(left_x,left_y));
        points.add(new PointF(right_x,right_y));
        points.add(new PointF(bottom_x,bottom_y));

        return path;
    }

    private static Path get_mouse_path(JSONObject landmark, float rate, int width, int height, List<PointF> pfs, int facing) throws JSONException {
        /*
        * "mouth_upper_lip_top": {
				"x": 93,
				"y": 277
			},
			"mouth_middle": {
				"x": 77,
				"y": 276
			},
			"mouth_lower_lip_bottom": {
				"x": 61,
				"y": 274
			},

			"mouth_left_corner": {
				"x": 81,
				"y": 243
			},
			"mouth_right_corner": {
				"x": 72,
				"y": 291
			}
        * */
        JSONObject mouth_upper_lip_top      = landmark.getJSONObject("mouth_upper_lip_top"   );
        JSONObject mouth_middle             = landmark.getJSONObject("mouth_middle" );
        JSONObject mouth_lower_lip_bottom   = landmark.getJSONObject("mouth_lower_lip_bottom");
        JSONObject mouth_left_corner        = landmark.getJSONObject("mouth_left_corner"  );
        JSONObject mouth_right_corner       = landmark.getJSONObject("mouth_right_corner"  );
        Path path = new Path();
        path.reset();
        int left_x = trans(mouth_left_corner.getInt("x"), rate, width, facing);
        int left_y = transY(mouth_left_corner.getInt("y"), rate, height, facing);
        path.moveTo(left_x, left_y);
        int top_x = trans(mouth_upper_lip_top.getInt("x"), rate, width, facing);
        int top_y = transY(mouth_upper_lip_top.getInt("y"), rate, height, facing);
        path.lineTo(top_x, top_y);
        int right_x = trans(mouth_right_corner.getInt("x"), rate, width, facing);
        int right_y = transY(mouth_right_corner.getInt("y"), rate, height, facing);
        path.lineTo(right_x, right_y);
        int middle_x = trans(mouth_middle.getInt("x"), rate, width, facing);
        int middle_y = transY(mouth_middle.getInt("y"), rate, height, facing);
        path.lineTo(middle_x, middle_y);
        path.lineTo(left_x, left_y);
        int bottom_x = trans(mouth_lower_lip_bottom.getInt("x"), rate, width, facing);
        int bottom_y = transY(mouth_lower_lip_bottom.getInt("y"), rate, height, facing);
        path.lineTo(bottom_x, bottom_y);
        path.lineTo(right_x, right_y);

        pfs.add(new PointF(left_x,left_y));
        pfs.add(new PointF(top_x,top_y));
        pfs.add(new PointF(right_x,right_y));
        pfs.add(new PointF(bottom_x,bottom_y));
        pfs.add(new PointF(middle_x,middle_y));
        return path;
    }

    private static Path get_3pointer_path_middle(
            JSONObject landmark,String key,
            float rate,
            int width,
            int height, List<PointF> point, int facing) throws JSONException {

        int left_x = 0, middle_x = 0, right_x = 0;
        int left_y = 0, middle_y = 0, right_y = 0;
        // 左眼 - 左眼角坐标
        JSONObject left_eyebrow_left_corner = landmark.getJSONObject(key + "_left_corner");
        left_x = left_eyebrow_left_corner.getInt("x");
        left_y = left_eyebrow_left_corner.getInt("y");

        // 左眼 - 眼中间
        JSONObject left_eyebrow_middle = landmark.getJSONObject(key + "_middle");
        middle_x = left_eyebrow_middle.getInt("x");
        middle_y = left_eyebrow_middle.getInt("y");

        // 左眼 - 右眼角坐标
        JSONObject left_eyebrow_right_corner = landmark.getJSONObject(key + "_right_corner");
        right_x = left_eyebrow_right_corner.getInt("x");
        right_y = left_eyebrow_right_corner.getInt("y");

        // 绘制成路径
        Path path = new Path();
        path.reset();
        path.moveTo(left_x = trans(left_x,rate,width,facing ),left_y = transY(left_y,rate,height, facing));
        path.lineTo(middle_x = trans(middle_x,rate,width,facing),middle_y = transY(middle_y,rate,height, facing));
        path.lineTo(right_x = trans(right_x,rate,width,facing),right_y = transY(right_y,rate,height, facing));

        point.add(new PointF(right_x,right_y));
        point.add(new PointF(middle_x,middle_y));
        point.add(new PointF(left_x,left_y));
        return path;
    }

    private static Path get_3pointer_path_center(
            JSONObject landmark,String key,
            float rate,
            int width,
            int height, List<PointF> points, int facing) throws JSONException {

        int left_x = 0, center_x = 0,  right_x = 0;
        int left_y = 0, center_y = 0,  right_y = 0;

        JSONObject left_eyebrow_left_corner = landmark.getJSONObject(key + "_left_corner");
        left_x = left_eyebrow_left_corner.getInt("x");
        left_y = left_eyebrow_left_corner.getInt("y");

        JSONObject _center = landmark.getJSONObject(key + "_center");
        center_x = _center.getInt("x");
        center_y = _center.getInt("y");

        JSONObject left_eyebrow_right_corner = landmark.getJSONObject(key + "_right_corner");
        right_x = left_eyebrow_right_corner.getInt("x");
        right_y = left_eyebrow_right_corner.getInt("y");

        // 绘制成路径
        Path path = new Path();
        path.reset();
        path.moveTo(left_x = trans(left_x,rate,width, facing),left_y = transY(left_y,rate,height, facing));
        path.lineTo(center_x = trans(center_x,rate,width, facing),center_y = transY(center_y,rate,height, facing));
        path.lineTo(right_x = trans(right_x,rate,width, facing),right_y = transY(right_y,rate,height, facing));

        points.add(new PointF(center_x,center_y));
        points.add(new PointF(right_x,right_y));
        points.add(new PointF(left_x,left_y));
        return path;
    }


    public static int trans(int x_y, float rate, int width_height, int facing){
        return (int) (width_height - (x_y * rate));
    }

    public static int transX(int x_y, float rate, int width_height, int facing){
        return (int) (width_height - (x_y * rate));
    }

    public static int transY(int x_y, float rate, int width_height, int facing){
        if(facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return (int)(x_y * rate);

        return (int) (width_height - (x_y * rate));
    }
}
