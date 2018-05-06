package com.example.facedemo.msc;

import android.view.View;

public interface IFaceDrawer {

    View getView();

    void onUpdateFace(String desc);
}
