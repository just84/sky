import com.google.common.collect.Lists;
import games.crossword.Crosswords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import use.forecast.stock.StockInfo;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static misc.s.LIS.testLIS;

/**
 * Created by yibin on 16/3/15.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception{
//        testCrosswords();
    }

    private static void testCrosswords() {
        Crosswords crosswords = new Crosswords("" +
                "0 0 0 0 1 1 1 1 1;" +
                "0 2 0 0 1 1 3 3 1;" +
                "2 2 0 0 1 3 3 3 3;" +
                "4 2 2 2 5 5 5 5 3;" +
                "4 2 2 2 5 6 6 6 3;" +
                "4 5 5 5 5 6 6 6 3;" +
                "4 4 4 4 7 8 8 6 6;" +
                "7 4 4 7 7 8 8 6 8;" +
                "7 7 7 7 7 8 8 8 8;");
        crosswords.addExtraPart("0 0,0 3,0 6,3 0,3 3,3 6,6 0,6 3,6 6");
        crosswords.addExtraPart("0 1,0 4,0 7,3 1,3 4,3 7,6 1,6 4,6 7");
        crosswords.addExtraPart("0 2,0 5,0 8,3 2,3 5,3 8,6 2,6 5,6 8");
        crosswords.addExtraPart("1 0,1 3,1 6,4 0,4 3,4 6,7 0,7 3,7 6");
        crosswords.addExtraPart("1 1,1 4,1 7,4 1,4 4,4 7,7 1,7 4,7 7");
        crosswords.addExtraPart("1 2,1 5,1 8,4 2,4 5,4 8,7 2,7 5,7 8");
        crosswords.addExtraPart("2 0,2 3,2 6,5 0,5 3,5 6,8 0,8 3,8 6");
        crosswords.addExtraPart("2 1,2 4,2 7,5 1,5 4,5 7,8 1,8 4,8 7");
        crosswords.addExtraPart("2 2,2 5,2 8,5 2,5 5,5 8,8 2,8 5,8 8");
        List<String> list = Lists.newArrayList();
        putIn2(list);
        long start = System.currentTimeMillis();
        crosswords.inputLines(list);
        crosswords.calculate();
        System.out.println("==================cost " + (System.currentTimeMillis() - start));
        crosswords.showTable();
    }

    private static void putIn0(List<String> list) {
        list.add("0 0 0 5 0 0 0 6 0");
        list.add("8 0 9 0 0 0 0 1 0");
        list.add("1 6 0 0 8 7 0 0 0");
        list.add("3 0 0 0 2 6 0 0 0");
        list.add("0 0 7 0 1 0 6 0 0");
        list.add("0 0 0 8 5 0 0 0 3");
        list.add("0 0 0 4 7 0 0 2 1");
        list.add("0 4 0 0 0 0 9 0 8");
        list.add("0 8 0 0 0 3 0 0 0");
    }

    private static void putIn1(List<String> list) {
        list.add("0 4 0 1 5 0 0 8 3");
        list.add("0 3 0 0 6 0 5 0 0");
        list.add("6 0 0 0 0 0 0 0 9");
        list.add("0 5 0 0 0 0 0 0 0");
        list.add("1 0 0 7 0 8 0 0 2");
        list.add("0 0 0 0 0 0 0 6 0");
        list.add("5 0 0 0 0 0 0 0 4");
        list.add("0 0 4 0 8 0 0 7 0");
        list.add("8 6 0 0 2 4 0 9 0");
    }

    private static void putIn2(List<String> list) {
        list.add("0 0 0 0 6 0 0 9 7");
        list.add("0 0 0 0 0 0 0 0 8");
        list.add("0 0 0 0 1 0 0 0 3");
        list.add("0 0 0 0 0 0 0 0 0");
        list.add("0 4 1 8 0 5 2 7 0");
        list.add("0 0 0 0 0 0 0 0 0");
        list.add("2 0 0 0 3 0 0 0 0");
        list.add("6 0 0 0 0 0 0 0 0");
        list.add("7 9 0 0 4 0 0 0 0");
    }
}


