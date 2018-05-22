package com.example.javasample.semaphore;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Tester {

    private static Player player;
    private static Player player2;

    public static void main(String[] args) {
        final Tester tester = new Tester();
        final ExecutorService pool = Executors.newCachedThreadPool();


        pool.execute(new Runnable() {
            @Override
            public void run() {
                boolean flag = true;
                String command = null;
                while (flag){
                    command = inputString("input command");
                    switch (command){
                        case "1":
                            pool.execute(player);
                            break;
                        case "0":
                            pool.execute(player2);
                            break;
                        case "2":
                            tester.release();
                            break;
                        case "3":
                            flag = false;
                            break;
                        case "4":
                            try {
                                System.err.println("before create player1");
                                player = new Player(1,tester.getRes());
                                System.err.println("after create player1");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "5":
                            try {
                                System.err.println("before create player2");
                                player2 = new Player(2,tester.getRes());
                                System.err.println("after create player2");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        });

        try {
            System.err.println("before create player1");
            // player = new Player(1, tester.getRes());
            boolean acquire = tester.tryAcquire();
            System.err.println("after create player1"+acquire);
            System.err.println("before create player2");
          /*  player2 = new Player(2, tester.getRes());*/
            boolean acquire2 = tester.tryAcquire();
            System.err.println("after create player2 "+acquire2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Scanner sScanner = new Scanner(System.in);
    private static String inputString(String hint){
        System.out.print(hint+": ");
        return sScanner.nextLine();
    }

    private Semaphore mSemaphore = new Semaphore(1);

    private Object getRes() throws InterruptedException {
        System.err.println("before getRes");
        mSemaphore.acquire(1);
        System.err.println("after getRes");
        return mCamera;
    }

    private boolean tryAcquire(){
        return mSemaphore.tryAcquire(1);
    }

    private void release(){
        System.err.println("release");
        mSemaphore.release(1);
    }

    private Object mCamera = new Object(){
        @Override
        public String toString() {

            return "monitor camera object";
        }
    };

    private static class Player implements Runnable{

        private final int id;
        private final Object res;

        Player(int id, Object res){
            this.id = id;
            this.res = res;
        }
        @Override
        public void run() {
            String s = res.toString();
            System.out.println("id: "+id+", string: "+s);
        }

    }
}
