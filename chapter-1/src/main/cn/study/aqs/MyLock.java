package main.cn.study.aqs;


import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * Lock接口实现 独享锁
 */
public class MyLock implements Lock {
    //锁的拥有者
    AtomicReference<Thread> ower = new AtomicReference<>();

    //等待队列
    LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>();


    @Override
    public boolean tryLock() {
        return ower.compareAndSet(null,Thread.currentThread());
    }

    @Override
    public void lock() {
        boolean flag = true; //标志位，代表已入队列
        while (!tryLock()) {
            if (flag) {//如果没有拿到锁，则入队列，且此线程等待
                queue.offer(Thread.currentThread());
                flag =false;
                LockSupport.park();
            } else {//移出等待队列
                queue.remove(Thread.currentThread());
            }
        }
    }

    @Override
    public void unlock() {
        if(ower.compareAndSet(Thread.currentThread(),null)){
            Iterator<Thread> iterator = queue.iterator();
            while(iterator.hasNext()){
                Thread waitor = iterator.next();
                LockSupport.unpark(waitor);
            }
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public Condition newCondition() {
        return null;
    }
}
