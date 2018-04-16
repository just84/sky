package misc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yibin on 18/4/9.
 */
public class BlockingSingleQueue<T> {
    T data = null;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public T take() throws InterruptedException {
        try {
            lock.lock();
            while (data == null) {
                System.out.println("take waiting");
                notEmpty.await();
            }
            notFull.signal();
            T r = data;
            data = null;
            return r;
        } finally {
            lock.unlock();
        }
    }

    public void put(T d) throws InterruptedException {
        try {
            lock.lock();
            while (data != null){
                System.out.println("put waiting");
                notFull.await();
            }
            data = d;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
}
