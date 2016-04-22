package utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

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
}
