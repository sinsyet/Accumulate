package com.example.appbase.engine;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static void run(Runnable r){
        if(r == null) return;

        pool.execute(r);
    }
}
