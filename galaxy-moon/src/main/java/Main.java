import com.google.common.collect.Lists;
import games.crossword.Crosswords;
import misc.BlockingSingleQueue;
import misc.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.A;

import java.io.BufferedReader;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Created by yibin on 16/3/15.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public void test(){
        System.out.println("hello world");
    }

    public static void main(String[] args) throws Exception {
//        testDeadLock();
//        testBlockingQueue();
//        testHeapSort();
        testNio();

    }

    private static void testNio() throws Exception{
        FileChannel fileChannel = new RandomAccessFile("/Users/yibin/testNio.txt","rw").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        int readSize = fileChannel.read(byteBuffer);
        byte[] b = new byte[1024];
        int index= 0;
        while(readSize != -1){
            System.out.println("readSize:"+readSize);
            byteBuffer.flip();
            byteBuffer.get(b,index,readSize);
            index += readSize;
            System.out.println(new String(b,0,index));
            byteBuffer.clear();
            readSize = fileChannel.read(byteBuffer);
        }
    }

    private static void testHeapSort() {
        List<Integer> s = new ArrayList<Integer>();
        for(int i = 0; i < 10 ; i++){
            s.add((int)(Math.random() * 100));
        }
        System.out.println(s);
        List<Integer> minHeap = new ArrayList<Integer>(s.size());
        for(Integer i : s){
            inputIntoMinHeap(minHeap, i);
        }
        List<Integer> result = new ArrayList<Integer>();
        System.out.println(minHeap);
        for(int i = 0; i < minHeap.size() ; i++){
            result.add(popMin(minHeap));
        }
        System.out.println(result);
    }

    private static Integer popMin(List<Integer> minHeap) {
        int result = minHeap.get(0);
        int findex = 0;
        int lcindex,rcindex;
        while (true){
            minHeap.set(findex,null);
            lcindex = getlcIndex(findex);
            rcindex = getrcIndex(findex);
            boolean havel = lcindex < minHeap.size() && minHeap.get(lcindex) != null;
            boolean haver = rcindex < minHeap.size() && minHeap.get(rcindex) != null;
            if(!havel && !haver){
                break;
            }
            if(havel && haver){
                if(minHeap.get(lcindex) > minHeap.get(rcindex)){
                    minHeap.set(findex,minHeap.get(rcindex));
                    findex = rcindex;
                } else {
                    minHeap.set(findex,minHeap.get(lcindex));
                    findex = lcindex;
                }
                continue;
            }
            if(havel){
                minHeap.set(findex,minHeap.get(lcindex));
                findex = lcindex;
            }
            if(haver){
                minHeap.set(findex,minHeap.get(rcindex));
                findex = rcindex;
            }
        }
        return result;
    }

    private static int getrcIndex(int findex) {
        return 2 * findex + 2;
    }

    private static int getlcIndex(int findex) {
        return 2 * findex + 1;
    }

    private static void inputIntoMinHeap(List<Integer> minHeap, Integer i) {
        minHeap.add(i);
        int index = minHeap.size() - 1;
        while(index > 0 && compareAndSwap(minHeap,index)){
            index = getFatherIndex(index);
        }
    }

    private static int getFatherIndex(int index) {
        return (index - 1) / 2;
    }

    private static boolean compareAndSwap(List<Integer> minHeap, int index) {
        int findex = getFatherIndex(index);
        if(minHeap.get(findex) > minHeap.get(index)){
            int i = minHeap.get(findex);
            minHeap.set(findex,minHeap.get(index));
            minHeap.set(index,i);
            return true;
        }
        return false;
    }

    public static void testDeadLock() throws Exception {
        ExecutorService executorService = ThreadPoolManager.newThreadPoolIfAbsent(
                "a", 24, 24, 24
        );
        Map<Integer, Source> sourceMap = new HashMap<Integer, Source>();
        Map<Integer, Source> sourceMapV2 = new HashMap<Integer, Source>();
        for (int i = 0; i < 16; i++) {
            sourceMap.put(i, new Source(i, new Semaphore(1)));
            sourceMapV2.put(i, new Source(i, new Semaphore(1)));
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < 12; i++) {
            Set<Integer> random = new HashSet<Integer>();
            while (random.size() < 4) {
                random.add((int) (Math.random() * 16));
            }
            System.out.println("task " + i + " : " + random.toString());
            executorService.execute(new SourceTask(i, start, new ArrayList<Integer>(random), sourceMap));
            executorService.execute(new SourceTaskV2(i, start, new ArrayList<Integer>(random), sourceMapV2));
        }
        executorService.shutdown();
    }

    static class SourceTaskV2 implements Runnable {
        private int id;
        private long start;
        private List<Integer> sources;
        private List<Integer> holdingSources;
        private Map<Integer, Source> sourceMap;

        public SourceTaskV2(int id, long start, List<Integer> sources, Map<Integer, Source> sourceMap) {
            this.sources = sources;
            Collections.sort(this.sources);
            this.holdingSources = new ArrayList<Integer>();
            this.sourceMap = sourceMap;
            this.start = start;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Source need = null;
                while (holdingSources.size() < sources.size()) {
                    if (need != null) {
                        need.preAcquire(id);
                    }
                    need = null;
                    Source target = sourceMap.get(sources.get(holdingSources.size()));
                    if (target.tryAcquire(id)) {
                        holdingSources.add(sources.get(holdingSources.size()));
                    } else {
                        need = target;
                        releaseAll();
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseAll();
            System.out.println("taskV2 " + id + " finished ,total:" + (System.currentTimeMillis() - start));
        }

        private void releaseAll() {
            for (int i = holdingSources.size() - 1; i >= 0; i--) {
                sourceMap.get(holdingSources.get(i)).release(id);
                holdingSources.remove(i);
            }
        }
    }

    static class SourceTask implements Runnable {
        private int id;
        private long start;
        private List<Integer> sources;
        private List<Integer> holdingSources;
        private Map<Integer, Source> sourceMap;

        public SourceTask(int id, long start, List<Integer> sources, Map<Integer, Source> sourceMap) {
            this.sources = sources;
            Collections.sort(this.sources);
            this.holdingSources = new ArrayList<Integer>();
            this.sourceMap = sourceMap;
            this.start = start;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Source need = null;
                while (holdingSources.size() < sources.size()) {
                    sourceMap.get(sources.get(holdingSources.size())).acquire();
                    holdingSources.add(sources.get(holdingSources.size()));
//                    if (need != null) {
//                        need.preAcquire(id);
//                    }
//                    need = null;
//                    Source target = sourceMap.get(sources.get(holdingSources.size()));
//                    if (target.tryAcquire(id)) {
//                        holdingSources.add(sources.get(holdingSources.size()));
//                    } else {
//                        need = target;
//                        releaseAll();
//                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseAll();
            System.out.println("task " + id + " finished ,total:" + (System.currentTimeMillis() - start));
        }

        private void releaseAll() {
            for (int i = holdingSources.size() - 1; i >= 0; i--) {
                sourceMap.get(holdingSources.get(i)).release(id);
                holdingSources.remove(i);
            }
        }
    }

    static class Source {
        private Integer id;
        private Semaphore semaphore;
        private Semaphore preSemaphore;

        public Source(Integer id, Semaphore semaphore) {
            this.id = id;
            this.semaphore = semaphore;
            this.preSemaphore = new Semaphore(0);
        }

        public void release(int taskid) {
//            System.out.println("task " + taskid + " release source " + id);
            semaphore.release();
            preSemaphore.release();
        }

        public void acquire() throws InterruptedException{
            semaphore.acquire();
        }

        public boolean tryAcquire(int taskid) {
            boolean result = semaphore.tryAcquire();
//            System.out.println("task " + taskid + " tryAcquire source " + id + result);
            return result;
        }

        public void preAcquire(int taskid) throws InterruptedException {
//            System.out.println("task " + taskid + " preAcquire source " + id);
            preSemaphore.acquire();
        }
    }

    public static void testWaitNotifyV2() throws Exception {
        ExecutorService executorService = ThreadPoolManager.newThreadPoolIfAbsent(
                "a", 3, 3, 2
        );
        TaskV2 a = new TaskV2("a");
        TaskV2 b = new TaskV2("b");
        a.setOther(b);
        b.setOther(a);

        executorService.execute(a);
        executorService.execute(b);
        Thread.sleep(1);
        synchronized (a) {
            a.notifyAll();
        }

        Thread.sleep(20);
        executorService.shutdownNow();
    }

    public static void testWaitNotify() {
        ExecutorService executorService = ThreadPoolManager.newThreadPoolIfAbsent(
                "a", 3, 3, 2
        );

        final String s = "";
        final long start = System.currentTimeMillis();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    synchronized (s) {
                        System.out.println("sn " + (System.currentTimeMillis() - start));
                        s.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (s) {
                    try {
                        System.out.println("s1 " + (System.currentTimeMillis() - start));
                        s.wait(1000);
                        Thread.sleep(1000);
                        System.out.println("s1 end " + (System.currentTimeMillis() - start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (s) {
                    try {
                        System.out.println("s2 " + (System.currentTimeMillis() - start));
                        s.wait(1000);
                        Thread.sleep(1000);
                        System.out.println("s2 end " + (System.currentTimeMillis() - start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.shutdown();
    }

    static class TaskV2 implements Runnable {
        private String name = "";
        private TaskV2 other;

        public void setOther(TaskV2 other) {
            this.other = other;
        }

        TaskV2(String name) {
            this.name = name;
        }

        public void release() {
            synchronized (other) {
                other.notifyAll();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (this) {
                        wait(10);
                    }
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Task " + name);
                other.release();
            }
        }
    }

    static class Task implements Runnable {
        private Semaphore semaphore = new Semaphore(0);
        private String name = "";
        private Task other;

        public void setOther(Task other) {
            this.other = other;
        }

        Task(String name) {
            this.name = name;
        }

        public void release() {
            semaphore.release();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Task " + name);
                other.release();
            }
        }
    }

    private static void testBlockingQueue() throws Exception {
        //        final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        final BlockingSingleQueue<String> queue = new BlockingSingleQueue<String>();
        final long now = System.currentTimeMillis();
        ExecutorService executorService = ThreadPoolManager.newThreadPoolIfAbsent(
                "a", 5, 5, 1);
        Runnable take = (new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("taking");
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Runnable put = (new Runnable() {
            @Override
            public void run() {
                try {
                    long i = System.currentTimeMillis() - now;
                    System.out.println("putting " + i);
                    queue.put(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.execute(take);
        Thread.sleep(100);
        executorService.execute(take);
        Thread.sleep(50);
        executorService.execute(put);
        Thread.sleep(20);
        executorService.execute(put);
        executorService.shutdown();
    }

    private void testRWLock() throws Exception {
        final misc.ReadWriteLock readWriteLock = new misc.ReadWriteLock();
        final long now = System.currentTimeMillis();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    readWriteLock.readLock();
                    System.out.println(System.currentTimeMillis() - now + " r reading...");
                    Thread.sleep(20);
                    readWriteLock.readUnLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable w = new Runnable() {
            @Override
            public void run() {
                try {
                    readWriteLock.writeLock();
                    System.out.println(System.currentTimeMillis() - now + " w writing...");
                    Thread.sleep(50);
                    readWriteLock.writeUnLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(r).start();
        Thread.sleep(5);
        new Thread(r).start();
        Thread.sleep(5);
        new Thread(r).start();
        Thread.sleep(5);
        new Thread(w).start();
        Thread.sleep(5);
        new Thread(w).start();
        Thread.sleep(50);
        new Thread(r).start();

//        final ReentrantLock lock = new ReentrantLock();
//        final Condition condition = lock.newCondition();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                lock.lock();
//                    try {
//                        System.out.println("start");
//                        condition.await();
//                        System.out.println("waiting end");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                lock.unlock();
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                lock.lock();
//                    try {
//                        System.out.println("start2");
//                        Thread.sleep(1000);
//                        System.out.println("wake up");
//                        condition.signal();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                lock.unlock();
//            }
//        }).start();
    }

    private static Integer getR(List<Integer> l) {
        Integer i = 0;
        for (int li : l) {
            i ^= li;
        }
        return i;
    }

    private static List<Integer> makeRandomList(int length) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        HashMap hashMap = new HashMap();

        hashMap.put(null, null);
        concurrentHashMap.get(0);
        list.contains(1);
        Map<Double, Integer> map = new TreeMap<Double, Integer>();
        for (int i = 0; i < length; i++) {
            Double key = Math.random();
            while (map.containsKey(key)) {
                key = Math.random();
            }
            map.put(key, i);
        }
        for (Double key : map.keySet()) {
            list.add(map.get(key));
        }

        return list;
    }

    private static void testCrosswords() {
        Crosswords crosswords = new Crosswords("" +
                "0 0 0 0 1 1 1 1 1;" +
                "0 2 0 0 1 1 3 3 1;" +
                "2 2 0 0 1 3 3 3 3;" +
                "4 2 2 2 5 5 5 5 3;" +
                "4 2 2 2 5 6 6 6 3;" +
                "4 5 5 5 5 6 6 6 3;" +
                "4 4 4 4 7 8 8 6 6;" +
                "7 4 4 7 7 8 8 6 8;" +
                "7 7 7 7 7 8 8 8 8;");
        crosswords.addExtraPart("0 0,0 3,0 6,3 0,3 3,3 6,6 0,6 3,6 6");
        crosswords.addExtraPart("0 1,0 4,0 7,3 1,3 4,3 7,6 1,6 4,6 7");
        crosswords.addExtraPart("0 2,0 5,0 8,3 2,3 5,3 8,6 2,6 5,6 8");
        crosswords.addExtraPart("1 0,1 3,1 6,4 0,4 3,4 6,7 0,7 3,7 6");
        crosswords.addExtraPart("1 1,1 4,1 7,4 1,4 4,4 7,7 1,7 4,7 7");
        crosswords.addExtraPart("1 2,1 5,1 8,4 2,4 5,4 8,7 2,7 5,7 8");
        crosswords.addExtraPart("2 0,2 3,2 6,5 0,5 3,5 6,8 0,8 3,8 6");
        crosswords.addExtraPart("2 1,2 4,2 7,5 1,5 4,5 7,8 1,8 4,8 7");
        crosswords.addExtraPart("2 2,2 5,2 8,5 2,5 5,5 8,8 2,8 5,8 8");
        List<String> list = Lists.newArrayList();
        putIn2(list);
        long start = System.currentTimeMillis();
        crosswords.inputLines(list);
        crosswords.calculate();
        System.out.println("==================cost " + (System.currentTimeMillis() - start));
        crosswords.showTable();
    }

    private static void putIn0(List<String> list) {
        list.add("0 0 0 5 0 0 0 6 0");
        list.add("8 0 9 0 0 0 0 1 0");
        list.add("1 6 0 0 8 7 0 0 0");
        list.add("3 0 0 0 2 6 0 0 0");
        list.add("0 0 7 0 1 0 6 0 0");
        list.add("0 0 0 8 5 0 0 0 3");
        list.add("0 0 0 4 7 0 0 2 1");
        list.add("0 4 0 0 0 0 9 0 8");
        list.add("0 8 0 0 0 3 0 0 0");
    }

    private static void putIn1(List<String> list) {
        list.add("0 4 0 1 5 0 0 8 3");
        list.add("0 3 0 0 6 0 5 0 0");
        list.add("6 0 0 0 0 0 0 0 9");
        list.add("0 5 0 0 0 0 0 0 0");
        list.add("1 0 0 7 0 8 0 0 2");
        list.add("0 0 0 0 0 0 0 6 0");
        list.add("5 0 0 0 0 0 0 0 4");
        list.add("0 0 4 0 8 0 0 7 0");
        list.add("8 6 0 0 2 4 0 9 0");
    }

    private static void putIn2(List<String> list) {
        list.add("0 0 0 0 6 0 0 9 7");
        list.add("0 0 0 0 0 0 0 0 8");
        list.add("0 0 0 0 1 0 0 0 3");
        list.add("0 0 0 0 0 0 0 0 0");
        list.add("0 4 1 8 0 5 2 7 0");
        list.add("0 0 0 0 0 0 0 0 0");
        list.add("2 0 0 0 3 0 0 0 0");
        list.add("6 0 0 0 0 0 0 0 0");
        list.add("7 9 0 0 4 0 0 0 0");
    }
}


