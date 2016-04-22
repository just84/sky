package misc;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yibin on 16/4/8.
 */
public class Cache {
    private static final LoadingCache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
        @Override
        public String load(String s) throws Exception {
            return s;
        }
    });

    public static String getValue(String s){
        try {
            return cache.get(s);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
