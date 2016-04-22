import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import pojo.Hasaki;
import proxy.MyProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GzipUtils;

import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws Exception {
        MyProxyServer server = new MyProxyServer();
        server.start(8866);
    }

    public static class A{
        String i;
        int j;

        public A(){}
        public A(String a,int b){
            i=a;
            j=b;
        }
        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            A a = (A) o;

            if (j != a.j) return false;
            if (!i.equals(a.i)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i.hashCode();
            result = 31 * result + j;
            return result;
        }
    }
}
