import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class LeetCode {
    public static void main(String[] args) throws Exception {
//        BufferedReader reader = Files.newBufferedReader(new File(), Charsets.UTF_8);

        LeetCode leetCode = new LeetCode();
        long start = System.currentTimeMillis();
        System.out.println(leetCode.findKthNumber(
                719885387,
                209989719
        ));
        System.out.println(System.currentTimeMillis() - start);
    }

    public int findKthNumber(int n, int k) {
        int i = 1;
        while (k>1){
            i = getNext(i,n);
            k--;
        }
        return i;
    }

    private int getNext(long i, int n) {
        if(i*10<=n){
            return (int)(i*10);
        }
        i+=1;
        if(i>n){
            i=i/10+1;
        }
        while (i%10 == 0){
            i/=10;
        }
        return (int)i;
    }

}
