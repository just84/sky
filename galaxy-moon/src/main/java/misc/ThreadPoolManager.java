package misc;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by yibin on 16/5/3.
 */
public class ThreadPoolManager {

    private static final ConcurrentMap<String, ThreadPoolExecutor> threadPools = Maps.newConcurrentMap();
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                for (ThreadPoolExecutor tp : threadPools.values()) {
                    tp.shutdown();
                    try {
                        if (!tp.awaitTermination(5, TimeUnit.SECONDS)) {
                            tp.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        tp.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }));
    }

    public static ThreadPoolExecutor getThreadPool(String threadPool) {
        if (threadPool == null) {
            return null;
        }
        return threadPools.get(threadPool);
    }

    public static synchronized ThreadPoolExecutor newThreadPoolIfAbsent(String name, int core, int max, int queue) {
        ThreadPoolExecutor tp;
        if (null == (tp = threadPools.get(name))) {
            tp = _newThreadPool(name, core, max, queue, DEFAULT_REJECTION_HANDLER);
            threadPools.put(name, tp);
        }
        return tp;
    }

    public static synchronized ThreadPoolExecutor newThreadPoolIfAbsent(String name, int core, int max, int queue,
                                                                        RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolExecutor tp;
        if (null == (tp = threadPools.get(name))) {
            tp = _newThreadPool(name, core, max, queue, rejectedExecutionHandler);
            threadPools.put(name, tp);
        }
        return tp;
    }

    private static ThreadPoolExecutor _newThreadPool(String name, int cores, int max, int queues,
                                                     RejectedExecutionHandler rejectedExecutionHandler) {
        return new ThreadPoolExecutor(cores, max, 60, TimeUnit.SECONDS, queues == 0 ? new SynchronousQueue<Runnable>()
                : queues < 0 ? new LinkedBlockingQueue<Runnable>() : new ArrayBlockingQueue<Runnable>(queues),
                new NamedThreadFactory(name), rejectedExecutionHandler);
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("\n");
        for (Map.Entry<String, ThreadPoolExecutor> entry : threadPools.entrySet()) {
            ThreadPoolExecutor tp = entry.getValue();
            msg.append("Pool status:" + entry.getKey() + ", max:" + tp.getMaximumPoolSize() + ", core:"
                    + tp.getCorePoolSize() + ", largest:" + tp.getLargestPoolSize() + ", active:" + tp.getActiveCount()
                    + ", queue:" + tp.getQueue().size());
            msg.append("\n");
        }
        return msg.toString();
    }

    // handlers
    public static final RejectedExecutionHandler DEFAULT_REJECTION_HANDLER = new DefaultRejectionHandler();

    private static class DefaultRejectionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String status = String
                    .format("Thread pool is EXHAUSTED!"
                                    + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                                    + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)", Thread
                                    .currentThread().getName(), e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e
                                    .getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(),
                            e.isShutdown(), e.isTerminated(), e.isTerminating());

            throw new RejectedExecutionException(status);
        }
    }
}
