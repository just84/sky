package misc;

import javax.swing.plaf.synth.SynthViewportUI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yibin on 18/4/8.
 */
public class ReadWriteLock {
    private final AtomicInteger isReading = new AtomicInteger(0);
    private final AtomicBoolean isWriting = new AtomicBoolean(false);

    public void readLock() {
        try {
            while (isWriting.get()) {
                Thread.sleep(10);
            }
            isReading.set(isReading.get() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("readLock" + isReading.get());
    }

    public void readUnLock() {
        isReading.set(isReading.get() - 1);
        System.out.println("readUnLock" + isReading.get());
    }

    public synchronized void writeLock() {
        try {
            while (isWriting.get() || isReading.get() > 0) {
                System.out.println("waiting" + isWriting.get() + isReading.get());
                Thread.sleep(10);
            }
            isWriting.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeUnLock() {
        try {
            System.out.println("writeUnLock");
            if (isWriting.get()) {
                isWriting.set(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
