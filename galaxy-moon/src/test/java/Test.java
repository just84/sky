import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by yibin on 16/3/24.
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static LoadingCache<A, String> cache = CacheBuilder.newBuilder()
            .maximumSize(300000)
            .expireAfterWrite(10, TimeUnit.valueOf("MINUTES"))
            .build(new CacheLoader<A, String>() {
                @Override
                public String load(A s) throws Exception {
                    logger.info("key:{},refreshed", s);
                    return s.getI() + System.currentTimeMillis();
                }
            });

    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
    public static void todo(final String param, final CountDownLatch countDownLatch, final List<String> result) throws InterruptedException {
        ListenableFuture listenableFuture = listeningExecutorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(100);
                System.out.println("exec " + param);
                result.add(String.valueOf(param));
                System.out.println("exec "+param+" finished");
                return String.valueOf(param);
            }
        });
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println("success " + s);
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("failed");
                countDownLatch.countDown();
            }
        });
    }

    public static void main(String[] args) throws Exception {
//        Host.start();


    }

    public static class A{
        private static String i;
        private static int j;

        public A(){}
        public A(String a,int b){
            i=a;
            j=b;
        }
        public static String getI() {
            return i;
        }

        public static void setI(String pi) {
            i = pi;
        }

        public static int getJ() {
            return j;
        }

        public static void setJ(int pj) {
            j = pj;
        }
    }
}
