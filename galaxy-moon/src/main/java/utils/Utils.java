package utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yibin on 16/3/30.
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final Map<String, Splitter> splitterMap = Maps.newHashMap();

    public static void closeAll(Closeable... closeableItems){
        for(Closeable closeable : closeableItems){
            try{
                Closeables.close(closeable, false);
            } catch (IOException e){
                logger.error("close failed, closeableItem:{}", closeable.getClass(), e);
            }
        }
    }

    public static Splitter getSplitter(String splitString){
        if(!splitterMap.containsKey(splitString)){
            splitterMap.put(splitString,Splitter.on(splitString).omitEmptyStrings().trimResults());
        }
        return splitterMap.get(splitString);
    }

    public static Integer max(Iterable<Integer> data) {
        Integer max = Integer.MIN_VALUE;
        for(Integer e : data){
            max = max > e ? max : e;
        }
        return max;
    }

    public static Integer min(Iterable<Integer> data) {
        Integer min = Integer.MAX_VALUE;
        for(Integer e : data){
            min = min > e ? e : min;
        }
        return min;
    }

    public static Integer average(Iterable<Integer> data) {
        Integer sum = 0;
        Integer i = 0;
        for(Integer e : data){
            sum += e;
            i++;
        }
        return sum / i;
    }

    public static <T> Collection<Collection<T>> selectMfromN(Collection<T> N, int M){
        if(M <= 0 || N == null || N.isEmpty() || N.size() < M){
            return Lists.newArrayList();
        }
        Collection<Collection<T>> results = Sets.newHashSet();
        if(M == 1){
            for(T item : N){
                Set<T> result = Sets.newHashSet();
                result.add(item);
                results.add(result);
            }
            return results;
        }
        if(M == N.size()){
            results.add(N);
            return results;
        }

        Set<T> selected = Sets.newHashSet();
        for(T item : N){
            selected.add(item);
            Set<T> tempN = Sets.newHashSet();
            tempN.addAll(N);
            tempN.removeAll(selected);
            for (Collection<T> childResult : selectMfromN(tempN, M-1)) {
                Set<T> result = Sets.newHashSet();
                result.add(item);
                result.addAll(childResult);
                results.add(result);
            }
        }
        return results;
    }

    public static String string2MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            logger.error("", e);
            return "";
        }
        char[] charArray=str.toCharArray();
        byte[] byteArray=new byte[charArray.length];
        for (int i=0;i<charArray.length;i++)
            byteArray[i]=(byte)charArray[i];
        byte[] md5Bytes=md5.digest(byteArray);
        StringBuffer hexValue=new StringBuffer();
        for (int i=0;i<md5Bytes.length;i++){
            int val=((int)md5Bytes[i])&0xff;
            if (val<16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
