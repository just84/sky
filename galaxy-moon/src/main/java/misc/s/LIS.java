package misc.s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 2017/9/29.
 */
public class LIS {
    private static Map<Double,Integer> dataLenMap = new HashMap<Double, Integer>();
    private static Integer count = 0;
    private static Integer Fcount = 0;
    public static void testLIS() {
        int len = 10;
        List<Double> data = buildData(len);
        for (Double d : data){
            dataLenMap.put(d,-1);
        }
        int max = 1;
        for(int i = 0;i<len;i++){
            max = Math.max(max,F(i,data));
        }
        for(Double d : data){
            System.out.println(d + "\t" + dataLenMap.get(d));
        }
        System.out.println(max);
        System.out.println(Fcount);
        System.out.println(count);
    }

    private static Integer F(int index,List<Double> data){
        Fcount++;
        if(index == 0){
            dataLenMap.put(data.get(index),1);
            return 1;
        }
        Integer maxLen = 1;
        for(int i = 0; i<index;i++){
            if(data.get(i) < data.get(index)){
                int len = dataLenMap.get(data.get(i)) > 0 ? dataLenMap.get(data.get(i)) : F(i,data);
                count++;
                len++;
                maxLen = Math.max(maxLen,len);
            }
        }
        dataLenMap.put(data.get(index),maxLen);
        System.out.println("index:"+index+" len:"+maxLen);
        return maxLen;
    }

    private static List<Double> buildData(int length) {
        List<Double> data = new ArrayList<Double>(length);
        for(int i = 0 ; i<length;i++){
            data.add(Math.random());
        }
        return data;
    }
}
