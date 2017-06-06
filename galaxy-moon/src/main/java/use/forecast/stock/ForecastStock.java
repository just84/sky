package use.forecast.stock;

import api.Processor;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by yibin on 16/3/30.
 */
public class ForecastStock {
    private static final Logger logger = LoggerFactory.getLogger(ForecastStock.class);
    private static List<Integer> ranges = Lists.newArrayList(3, 5, 7, 10, 15);
    private static List<Integer> begins = Lists.newArrayList(30,56,73,90,120);

    public static void main(String[] args) throws Exception{
//        buyAndSell();
        dingtou();
    }

    /**
     * http://table.finance.yahoo.com/table.csv?s=
     * 000001.sz 平安银行
     */
    private static void buyAndSell() {

    }

    private static void dingtou() throws Exception{
        List<StockInfo> stockInfoList = FetchService.fetchHistoryData("000001","2017-06-01","2017-06-05");
        System.out.println(JsonUtils.serialize(stockInfoList));
    }

//    public static void forecast(String code, String market) {
//        List<StockInfo> stockInfoList = FetchService.fetchHistoryData(code, market, );
//        StockInfo recentStockInfo = FetchService.fetchNewestData(code, market);
//    }
//
//    private static void forecast() throws Exception{
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
//    }
//
//    private static void showForecast(List<StockInfo> stockInfoList, Integer begin, Integer index){
//        System.out.println("@"+stockInfoList.get(index).getDate());
//        for(int range : ranges){
//            System.out.println("after "+range+" day");
//            System.out.println("percent: "+ calculatePercent(stockInfoList, begin, begin - range) + "\t" + "forecastPercent: " + calculatePercent(stockInfoList, index, index - range));
//        }
//
//    }
//
//    private static Double calculatePercent(List<StockInfo> stockInfoList, Integer begin, Integer end){
//        return (stockInfoList.get(end).getClosePrice() - stockInfoList.get(begin).getClosePrice()) / stockInfoList.get(begin).getClosePrice();
//    }
}
