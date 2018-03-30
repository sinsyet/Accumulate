package com.example.sample;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShellService extends Service {

    private static final String TAG = "ShellService";

    public ShellService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        mTimer.schedule(tt, 30 * 1000, 60 * 1000);
    }

    private Timer mTimer = new Timer();

    // protected TimerTask(); 为什么能创建;
    // 因为我们创建的是内部类, 内部类是TimerTask的子类对象,
    // protected允许子类对象调用自身;
    // 只想说好机智
    private TimerTask tt = new TimerTask() {
        @Override
        public void run() {
            loadShell();
        }
    };

    private void loadShell() {
        File shellDir = new File(Environment.getExternalStorageDirectory(), "shell");
        if (!shellDir.exists() || shellDir.isFile()) {
            Log.e(TAG, "loadShell: " + shellDir.getAbsolutePath() + " not exist");
            return;
        }

        File[] files = shellDir.listFiles();
        if (files == null || files.length == 0) {
            Log.e(TAG, "loadShell: empty shell dir");
            return;
        }

        adbFiles.clear();
        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".adb")) {
                adbFiles.add(f);
            }
        }

        if (adbFiles.size() == 0) {
            Log.e(TAG, "loadShell: no executable adb file");
            return;
        }

        for (int i = 0; i < adbFiles.size(); i++) {
            try {
                File file = adbFiles.get(i);
                Log.e(TAG, "loadShell: " + file.getName());
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                OutputStream os = null;
                while ((line = reader.readLine()) != null) {
                    if (os == null) {
                        File result = new File(
                                shellDir,"/result");
                        if(!result.exists()){
                            boolean mkdir = result.mkdir();
                            Log.e(TAG, "loadShell: mkdir: "+mkdir);
                        }
                        File file_r = new File(result,file.getName()+".r");
                        if (file_r.exists()) {
                            // file_r.createNewFile();
                            file_r.delete();
                        }
                        String absolutePath = file_r.getAbsolutePath();
                        Log.e(TAG, "loadShell: path: "+absolutePath);
                        file_r.createNewFile();
                        os = new FileOutputStream(
                                file_r);
                        Log.e(TAG, "loadShell: " + file_r.getAbsolutePath());
                    }

                    shell(os, line);
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e) {
                    }
                    os = null;
                }
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<File> adbFiles = new ArrayList<>();

    public static void shell(String... shells) {
        if (shells.length == 0) return;
        Runtime runtime = Runtime.getRuntime();
        BufferedReader reader = null;
        for (int i = 0; i < shells.length; i++) {
            System.out.println(shells[i]);
            try {
                Process exec = runtime.exec(shells[i]);
                // if (reader == null) {
                // reader 应该是每个shell命令有一个; 而不是多个shell命令用同一个输入流
                InputStreamReader streamReader = new InputStreamReader(exec.getInputStream(), "GBK");
                System.err.println(streamReader.getEncoding());
                reader = new BufferedReader(streamReader);
                // }

                String s = null;
                while ((s = reader.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void shell(OutputStream os, String... shells) {
        if (shells.length == 0) return;
        Runtime runtime = Runtime.getRuntime();
        BufferedReader reader = null;
        for (int i = 0; i < shells.length; i++) {
            System.out.println(shells[i]);
            try {
                Process exec = runtime.exec(shells[i]);

                // reader 应该是每个shell命令有一个; 而不是多个shell命令用同一个输入流
                InputStreamReader streamReader = new InputStreamReader(exec.getInputStream(),
                        "UTF-8");
                System.err.println(streamReader.getEncoding());
                reader = new BufferedReader(streamReader);

                String s = null;
                os.write(shells[i].getBytes("UTF-8"));
                os.write("\n".getBytes("UTF-8"));
                while ((s = reader.readLine()) != null) {
                    // System.out.println(s);
                    os.write(s.getBytes("UTF-8"));
                    os.write("\n".getBytes("UTF-8"));
                    os.flush();
                }
                os.write("\r\n".getBytes("UTF-8"));
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
