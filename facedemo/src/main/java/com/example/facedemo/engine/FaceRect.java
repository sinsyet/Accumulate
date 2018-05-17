package com.example.facedemo.engine;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author MatrixCV
 *         FaceRect�����ڱ�ʾ�������Ľ�������а����� �����ĽǶȡ��÷֡�����λ�á��ؼ���
 */
public class FaceRect {
	
	public float score;
	public Rect bound = new Rect();
	public Point point[];

	public Rect raw_bound = new Rect();
	public Point raw_point[];

	@Override
	public String toString() {
		return bound.toString();
	}
}