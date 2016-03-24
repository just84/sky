package utils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by yibin on 16/3/22.
 */
public class SortUtils {

    public static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if(high == low || high - low == 1){
            return;
        }
        if(high - low == 2){
            if(comparator.compare(list.get(high-1),list.get(low)) < 0){
                swap(list,high-1,low);
            }
            return ;
        }
        int i = low,j = high;
        int mark = 0;
        while(i<j){
            i++;
            j--;
            while (comparator.compare(list.get(i),list.get(low)) < 0 && i<j){
                mark = 0;
                i++;
            }
            while (comparator.compare(list.get(j),list.get(low)) > 0 && i<j){
                mark = 1;
                j--;
            }
            if(i<j){
                swap(list,i,j);
            }
        }
        swap(list,i-mark,low);
        quickSort(list, low, i - mark , comparator);
        quickSort(list, i + 1 - mark, high, comparator);
    }

    public static <T> void insertSort(List<T> list, int low, int high, Comparator<T> comparator) {
        for(int i = low;i<high;i++){
            int j = i;
            while(j>low && comparator.compare(list.get(j),list.get(j-1)) < 0){
                swap(list,j,j-1);
                j--;
            }
        }
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i,list.get(j));
        list.set(j,temp);
    }
}
