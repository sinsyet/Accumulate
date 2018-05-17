package com.example.facedemo.msc;

import android.graphics.Path;
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

        public List<Path> getLandmarks() {
            return landmarks;
        }

        public Path getPosition() {
            return position;
        }
    }

    public int getCount() {
        return count;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public static FaceR decode(String s){
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
            int bottom = 0, right = 0, left = 0, top = 0;
            for (int i = 0; i < len; i++) {
                JSONObject faceJSONObject = face.getJSONObject(i);

                /*
                "position": {
                    "bottom": 341,
                    "right": 272,
                    "left": 40,
                    "top": 109
                },

                */
                JSONObject position = faceJSONObject.getJSONObject("position");
                bottom = position.getInt("bottom");
                right  = position.getInt("right" );
                left   = position.getInt("left"  );
                top    = position.getInt("top"   );

                // position
                Face f = new Face();
                Path p = f.position;
                p.moveTo(left,top);
                p.lineTo(right,top);
                p.lineTo(right,bottom);
                p.lineTo(left,bottom);

                r.faces.add(f);
            }
            return r;
        } catch (JSONException e) {

        }
        return null;
    }
}
