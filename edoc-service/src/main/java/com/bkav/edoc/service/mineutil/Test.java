package com.bkav.edoc.service.mineutil;

import java.util.ArrayList;

public class Test {
    static int count = 0;

    public static void main(String[] args) {

        ArrayList<Thread> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 200000; i++) {
                        count++;
                    }
                }
            });
            th.start();

            list.add(th);
        }

        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
            }
        }

        System.out.println("count: " + count);
    }
}
