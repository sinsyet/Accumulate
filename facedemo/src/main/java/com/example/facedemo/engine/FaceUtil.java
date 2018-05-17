package com.example.facedemo.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class FaceUtil {
	public final static int REQUEST_PICTURE_CHOOSE = 1;
	public final static int REQUEST_CAMERA_IMAGE = 2;
	public final static int REQUEST_CROP_IMAGE = 3;
	private static final String TAG = "FaceUtil";
	private static final File parentPath = Environment
			.getExternalStorageDirectory();
	private static String storagePath = "";
	private static final String DST_FOLDER_NAME = "jupos";
	private static String jpegName;

	/***
	 * �ü�ͼƬ
	 * 
	 * @param activity
	 *            Activity
	 * @param uri
	 *            ͼƬ��Uri
	 */
	public static void cropPicture(Activity activity, Uri uri) {
		Intent innerIntent = new Intent("com.android.camera.action.CROP");
		innerIntent.setDataAndType(uri, "image/*");
		innerIntent.putExtra("crop", "true");// ���ܳ�������С���򣬲�Ȼû�м������ܣ�ֻ��ѡȡͼƬ
		innerIntent.putExtra("aspectX", 1); // �Ŵ���С������X
		innerIntent.putExtra("aspectY", 1);// �Ŵ���С������X ����ı���Ϊ�� 1:1
		innerIntent.putExtra("outputX", 320); // ������������ͼƬ��С
		innerIntent.putExtra("outputY", 320);
		innerIntent.putExtra("return-data", true);
		// ��ͼ��С����������޺ڿ�
		innerIntent.putExtra("scale", true);
		innerIntent.putExtra("scaleUpIfNeeded", true);
		File imageFile = new File(
				getImagePath(activity.getApplicationContext()));
		innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		innerIntent.putExtra("outputFormat",
				Bitmap.CompressFormat.JPEG.toString());
		activity.startActivityForResult(innerIntent, REQUEST_CROP_IMAGE);
	}

	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * 
	 * @param path
	 *            ͼƬ����·��
	 * @return degree ��ת�Ƕ�
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * ��תͼƬ
	 * 
	 * @param angle
	 *            ��ת�Ƕ�
	 * @param bitmap
	 *            ԭͼ
	 * @return bitmap ��ת���ͼƬ
	 */
	public static Bitmap rotateImage(int angle, Bitmap bitmap) {
		// ͼƬ��ת����
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// �õ���ת���ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * ��ָ�������Ͻ����������
	 * 
	 * @param canvas
	 *            �����Ļ���
	 * @param face
	 *            ��Ҫ���Ƶ�������Ϣ
	 * @param width
	 *            ԭͼ��
	 * @param height
	 *            ԭͼ��
	 * @param frontCamera
	 *            �Ƿ�Ϊǰ������ͷ����Ϊǰ������ͷ�����ҶԳ�
	 * @param DrawOriRect
	 *            �ɻ���ԭʼ��Ҳ����ֻ���ĸ���
	 */
	static public void drawFaceRect(Canvas canvas, FaceRect face, int width,
			int height, boolean frontCamera, boolean DrawOriRect) {
		if (canvas == null) {
			return;
		}

		Paint paint = new Paint();
		paint.setColor(Color.rgb(56, 139, 243));
		int len = (face.bound.bottom - face.bound.top) / 8;
		if (len / 8 >= 2)
			paint.setStrokeWidth(len / 8);
		else
			paint.setStrokeWidth(2);

		Rect rect = face.bound;

		if (frontCamera) {
			int top = rect.top;
			rect.top = width - rect.bottom;
			rect.bottom = width - top;
		}

		if (DrawOriRect) {
			paint.setStyle(Style.STROKE);
			canvas.drawRect(rect, paint);
		} else {
			int drawl = rect.left - len;
			int drawr = rect.right + len;
			int drawu = rect.top - len;
			int drawd = rect.bottom + len;

			canvas.drawLine(drawl, drawd, drawl, drawd - len, paint);
			canvas.drawLine(drawl, drawd, drawl + len, drawd, paint);
			canvas.drawLine(drawr, drawd, drawr, drawd - len, paint);
			canvas.drawLine(drawr, drawd, drawr - len, drawd, paint);
			canvas.drawLine(drawl, drawu, drawl, drawu + len, paint);
			canvas.drawLine(drawl, drawu, drawl + len, drawu, paint);
			canvas.drawLine(drawr, drawu, drawr, drawu + len, paint);
			canvas.drawLine(drawr, drawu, drawr - len, drawu, paint);
		}

		// if (face.point != null) {
		// for (Point p : face.point) {
		// if (frontCamera) {
		// p.y = width - p.y;
		// }
		// canvas.drawPoint(p.x, p.y, paint);
		// }
		// }
	}

	/**
	 * ��������ԭͼ˳ʱ����ת90��
	 * 
	 * @param r
	 *            ����ת�ľ���
	 * 
	 * @param width
	 *            ������ζ�Ӧ��ԭͼ��
	 * 
	 * @param height
	 *            ������ζ�Ӧ��ԭͼ��
	 * 
	 * @return ��ת��ľ���
	 */
	static public Rect RotateDeg90(Rect r, int width, int height) {
		int left = r.left;
		r.left = height - r.bottom;
		r.bottom = r.right;
		r.right = height - r.top;
		r.top = left;
		return r;
	}

	/**
	 * ������ԭͼ˳ʱ����ת90��
	 * 
	 * @param p
	 *            ����ת�ĵ�
	 * 
	 * @param width
	 *            ������Ӧ��ԭͼ��
	 * 
	 * @param height
	 *            ������Ӧ��ԭͼ��
	 * 
	 * @return ��ת��ĵ�
	 */
	static public Point RotateDeg90(Point p, int width, int height) {
		int x = p.x;
		p.x = height - p.y;
		p.y = x;
		return p;
	}

	public static int getNumCores() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * ����ü���ͼƬ��·��
	 * 
	 * @return
	 */
	public static String getImagePath(Context context) {
		String path;

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = context.getFilesDir().getAbsolutePath();
		} else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/msc/";
		}

		if (!path.endsWith("/")) {
			path += "/";
		}

		File folder = new File(path);
		if (folder != null && !folder.exists()) {
			folder.mkdirs();
		}
		path += "ifd.jpg";
		return path;
	}

	/**
	 * ����Bitmap������
	 * 
	 * @param Bitmap
	 */
	public static void saveBitmapToFile(Context context, Bitmap bmp) {
		String file_path = getImagePath(context);
		File file = new File(file_path);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Bitmap��sdcard
	 * 
	 * @param b
	 */
	public static boolean saveBitmap(Bitmap b, Context context) {

		String jpegName = initPath(context, false);

		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 40, bos);// ѹ�������ͼƬ
			bos.flush();
			bos.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * ��ʼ������·��
	 * 
	 * @return
	 */
	public static String initPath(Context context, boolean flag) {
		if (flag) {
			return jpegName;
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			storagePath = context.getFilesDir().getAbsolutePath() + "/"
					+ DST_FOLDER_NAME;
			File f = new File(storagePath);
			if (!f.exists()) {
				f.mkdir();
			}
			long dataTake = System.currentTimeMillis();
			jpegName = storagePath + "/" + dataTake + ".jpg";

		} else {

			storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if (!f.exists()) {
				f.mkdir();
			}
			long dataTake = System.currentTimeMillis();
			jpegName = storagePath + "/" + dataTake + ".jpg";

		}

		return jpegName;
	}
}
