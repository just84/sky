import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.*;
import com.sun.javafx.binding.StringFormatter;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import games.landlords.*;
import games.landlords.groupTypes.Boom;
import games.landlords.groupTypes.GroupType;
import misc.ThreadPoolManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.A;
import pojo.B;
import pojo.TestPoj;
import utils.GzipUtils;
import utils.JsonUtils;
import utils.Utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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


//        Host.start();

        Cards targetCards = Cards.newCards("55533");
        Cards selfCards = Cards.newCards("22277766655443");
        System.out.println(LocalTools.getGroupType(targetCards).getSolution(selfCards, targetCards));

//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//
//        A a = new A();
//        a.setI("i");
//        a.setJ("j");
//        System.out.println(validator.validate(a));
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
}
