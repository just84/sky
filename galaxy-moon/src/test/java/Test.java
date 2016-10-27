import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.BinaryTreeTraverser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.*;
import misc.MoneyCalculator;
import misc.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.A;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by yibin on 16/3/24.
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static final Joiner lineJoiner = Joiner.on("_").skipNulls();
    private static LoadingCache<A, String> cache = CacheBuilder.newBuilder()
            .maximumSize(300000)
            .refreshAfterWrite(10, TimeUnit.MILLISECONDS)
            .build(new CacheLoader<A, String>() {
                @Override
                public String load(A s) throws Exception {
                    logger.info("key:{},value:{},refreshed", s, cache.getIfPresent(s));
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

//        testSynchronized();

        System.out.println(MoneyCalculator.debxByMouth(70000,7000,0.007,24,36));

//        Host.start();

//        Cards targetCards = Cards.newCards("55533");
//        Cards selfCards = Cards.newCards("22277766655443");
//        System.out.println(LocalTools.getGroupType(targetCards).getSolution(selfCards, targetCards));

//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//
//        A a = new A();
//        a.setI("i");
//        a.setJ("j");
//        System.out.println(validator.validate(a));
    }

    private static int test() {

        int a = 0;
        try {
            System.out.println("a");
            return a;
        } finally {
            System.out.println("b");
            a =1;
        }
    }

    public static String load(String code) throws Exception {
        if (Strings.isNullOrEmpty(code)) {
            return code;
        }
        if (code.charAt(2) != '0' || code.indexOf('0') == -1) {
            return code;
        }

        char[] bytes = new char[code.length()];
        bytes[0] = code.charAt(0);
        bytes[1] = code.charAt(1);

        int i = 2;
        int j = 2;

        for (; i < code.length(); i++) {
            if (code.charAt(i) != '0') {
                break;
            }
        }
        while (i < code.length()) {
            bytes[j++] = code.charAt(i++);
        }

        return new String(bytes, 0, j);
    }

    public static void testSynchronized(){

        final TestSynchronized testSynchronized = new TestSynchronized();
        final A a = new A();
        ThreadPoolExecutor executor = ThreadPoolManager.newThreadPoolIfAbsent("thread",5,5,10);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                testSynchronized.print1();
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                new TestSynchronized().print1();
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                TestSynchronized.print2();
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                TestSynchronized.print3();
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                testSynchronized.print4();
            }
        });
        executor.shutdown();
    }
}
