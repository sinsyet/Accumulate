package com.example.javasample.semaphore;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    static int randomInt(){
        Random random = new Random();
        return random.nextInt(10) + 2;
    }
    static class Student implements Runnable {

        private int num;
        private Playground playground;

        public Student(int num, Playground playground) {
            this.num = num;
            this.playground = playground;
        }

        @Override
        public void run() {

            try {
                //获取跑道
                Playground.Track track = playground.getTrack();
                if (track != null) {
                    System.out.println("Student " + num + " on " + track.toString() + "running");
                    TimeUnit.SECONDS.sleep(randomInt());
                    System.out.println("Student" + num + " release " + track.toString());
                    //释放跑道
                    playground.releaseTrack(track);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        // 60秒不用就会释放线程
        Executor executor = Executors.newCachedThreadPool();
        Playground playground = new Playground();
        for (int i = 0; i < 20; i++) {
            executor.execute(new Student(i+1,playground));
        }
        int i = Thread.activeCount();
        System.out.println("activeCount: " + i);

    }
}
