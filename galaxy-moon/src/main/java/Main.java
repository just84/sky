import api.Processor;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.sun.javafx.binding.StringFormatter;
import game.crossword.Crosswords;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import use.forecast.stock.CalculateService;
import use.forecast.stock.Constant;
import use.forecast.stock.FetchService;
import use.forecast.stock.StockInfo;
import utils.JsonUtils;

import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 16/3/15.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static List<Integer> ranges = Lists.newArrayList(3, 5, 7, 10, 15);
    public static List<Integer> begins = Lists.newArrayList(30,56,73,90,120);
    public static void main(String[] args) throws Exception{

        testCrosswords();

//        List<StockInfo> stockInfoList = FetchService.fetchFromFile(new File("/Users/yibin/Downloads/sh.csv"));
//
//        for(int begin : begins){
//            for (int range : ranges) {
//                int total = 0;
//                int upOrDownCorrect = 0;
//                List<Integer> upOrDownSimilarity = Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                List<Integer> s = Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//
//                for (int index = begin + range; index < stockInfoList.size() - range; index++) {
//                    double similarity = CalculateService.calculateCosSimilarity(stockInfoList, begin, index, range, new Processor<StockInfo, Double>() {
//                        @Override
//                        public Double process(StockInfo stockInfo) {
//                            return stockInfo.getPercent();
//                        }
//                    });
//                    int i = (int) (similarity * 10) + 10;
//                    s.set(i, s.get(i) + 1);
//                    Double p = CalculateService.calculateChangePercent(stockInfoList,begin,index,1);
//                    if(p>0){
//                        upOrDownCorrect++;
//                    }
//                    int j = (int) (p * 10) + 10;
//                    upOrDownSimilarity.set(j, upOrDownSimilarity.get(j) + 1);
//                    total++;
//                }
//
//                logger.info("\nbegin index:{}, used range:{}\ns:{}\nupOrDownCorrect/total:{}/{}\nupOrDownSimilarity:{}\n",
//                        begin, range, s, upOrDownCorrect, total, upOrDownSimilarity);
//            }
//        }
    }

    private static void showForecast(List<StockInfo> stockInfoList, Integer begin, Integer index){
        System.out.println("@"+stockInfoList.get(index).getDate());
        for(int range : ranges){
            System.out.println("after "+range+" day");
            System.out.println("percent: "+ calculatePercent(stockInfoList, begin, begin - range) + "\t" + "forecastPercent: " + calculatePercent(stockInfoList, index, index - range));
        }

    }

    private static Double calculatePercent(List<StockInfo> stockInfoList, Integer begin, Integer end){
        return (stockInfoList.get(end).getClosePrice() - stockInfoList.get(begin).getClosePrice()) / stockInfoList.get(begin).getClosePrice();
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


