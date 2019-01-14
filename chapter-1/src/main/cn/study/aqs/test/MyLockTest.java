package main.cn.study.aqs.test;

import main.cn.study.aqs.MyLock;

public class MyLockTest {

    int i = 0;

    MyLock lock = new MyLock();
    public void add(){
        lock.lock();
        try {
            i++;
        }finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        MyLockTest demo = new MyLockTest();
        int n = 3;
        for(int i=0;i<n;i++){
            new Thread(()->{
                for(int j=0;j<10000;j++){
                    demo.add();
                }
            }).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(demo.i);
    }

}
