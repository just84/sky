package use.forecast.stock;

import api.Processor;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 16/3/30.
 */
public class ForecastStock {
    private static final Logger logger = LoggerFactory.getLogger(ForecastStock.class);

    public static void main(String[] args) throws Exception{
        buyAndSell();
        //定投只关注卖出时机
        dingtou1("000001","1994-01-01","2017-09-15");
//        dingtou2("000001","2016-01-01","2017-09-15");
    }

    /**
     * 交易策略
     * 目标：寻找最优的交易策略
     * todo：1.进化算法；2.特征选取(各种统计特征，尽量多)；3.得到最优交易策略
     */
    private static void buyAndSell() {

    }

    /**
     * 遍历定投周期1-31，卖出点：收益达到1-n%
     * 每期投入 周期*（总收入/总投入）
     * 结果：周期20-31，卖出点逼近100，单次交易数年，弃
     * @param code
     * @param start
     * @param end
     * @throws Exception
     */
    private static void dingtou1(String code, String start, String end) throws Exception{
        List<StockInfo> stockInfoList = FetchService.fetchHistoryData(code,start,end);
        if(stockInfoList == null || stockInfoList.size() == 0){
            System.out.println("no data");
            return;
        }

        List<Map<String,Object>> result = Lists.newArrayList();
        for(int zhouqi = 1; zhouqi < 31;zhouqi++){
            for(int sellpoint = 1; sellpoint < 100;sellpoint++){
                double totalin = 0;
                double totalout = 0;
                double times = 0;
                double hold = 0;
                double holdvalue = 0;
                int lastsellday = 0;
                List<Integer> days = Lists.newArrayList();
                for(int day =0;day<stockInfoList.size();day++){
                    if(stockInfoList.get(day).getClosePrice() * hold - holdvalue > holdvalue * sellpoint / 100){//超过卖点
                        totalin += stockInfoList.get(day).getClosePrice() * hold;
                        times ++;
                        hold = 0;
                        holdvalue = 0;
                        days.add(day - lastsellday);
                        lastsellday = day;
                    }
                    if(day % zhouqi == 0){//买入
//                        totalout += stockInfoList.get(day).getClosePrice();
//                        hold ++;
//                        holdvalue += stockInfoList.get(day).getClosePrice();
                        double out = zhouqi * (totalout > 0 ? (totalin + holdvalue) / totalout : 1);
                        totalout += out;
                        hold += out / stockInfoList.get(day).getClosePrice();
                        holdvalue += out;
                    }
                }
                Map<String,Object> map = Maps.newHashMap();
                map.put("totalin",totalin);
                map.put("totalout",totalout);
                map.put("times",times);
                map.put("hold",hold);
                map.put("holdvalue",holdvalue);
                map.put("zhouqi",zhouqi);
                map.put("sellpoint",sellpoint);
                map.put("minday",Utils.min(days));
                map.put("maxday",Utils.max(days));
                map.put("averageday",Utils.average(days));
                result.add(map);
                System.out.println(zhouqi+","+sellpoint);
            }
        }

        BufferedWriter writer = Files.newWriter(new File("D:\\projects\\mine\\"+code+"-"+start+end+".csv"), Charsets.UTF_8);
        Joiner j = Joiner.on(",");
        for(Map<String,Object> map : result){
            String s = j.join(map.get("totalin")
                    , map.get("totalout")
                    , map.get("times")
                    , map.get("hold")
                    , map.get("holdvalue")
                    , map.get("zhouqi")
                    , map.get("sellpoint")
                    , (Double)map.get("totalin") + (Double) map.get("holdvalue") - (Double)map.get("totalout")
                    , (Double)map.get("totalout") > 0 ? ((Double)map.get("totalin") + (Double) map.get("holdvalue")) / (Double)map.get("totalout") : 0
                    , map.get("minday")
                    , map.get("maxday")
                    , map.get("averageday")
            );
            writer.write(s);
            writer.newLine();
        }
        writer.close();

        System.out.println(code+","+stockInfoList.size());
    }

    /**
     * 遍历定投周期1-31，卖出点：收益超过1-n%，且近期跌幅达到1-m%
     * 每期投入 1+收益率
     * 结果：
     * @param code
     * @param start
     * @param end
     * @throws Exception
     */
    private static void dingtou2(String code, String start, String end) throws Exception{
        List<StockInfo> stockInfoList = FetchService.fetchHistoryData(code,start,end);
        if(stockInfoList == null || stockInfoList.size() == 0){
            System.out.println("no data");
            return;
        }

        List<Map<String,Object>> result = Lists.newArrayList();
        for(int zhouqi = 1; zhouqi < 31;zhouqi++){
            for(int sellpoint = 1; sellpoint < 100;sellpoint++){
                for(int recentDrop = 1; recentDrop < 20;recentDrop++) {
                    double totalin = 0;
                    double totalout = 0;
                    double times = 0;
                    double hold = 0;
                    double holdvalue = 0;
                    int lastsellday = 0;
                    double lastTopValue = 0;
                    List<Integer> days = Lists.newArrayList();
                    for (int day = 0; day < stockInfoList.size(); day++) {
                        if (stockInfoList.get(day).getClosePrice() * hold - holdvalue > holdvalue * sellpoint / 100
                                && (lastTopValue - stockInfoList.get(day).getClosePrice() > lastTopValue * recentDrop / 100)) {//卖点
                            totalin += stockInfoList.get(day).getClosePrice() * hold;
                            times++;
                            hold = 0;
                            holdvalue = 0;
                            days.add(day - lastsellday);
                            lastsellday = day;
                            lastTopValue = 0;
                        }
                        if (day % zhouqi == 0) {//买入
//                        totalout += stockInfoList.get(day).getClosePrice();
//                        hold ++;
//                        holdvalue += stockInfoList.get(day).getClosePrice();
                            double out = zhouqi * (totalout > 0 ? (totalin + holdvalue) / totalout : 1);
                            totalout += out;
                            hold += out / stockInfoList.get(day).getClosePrice();
                            holdvalue += out;
                        }
                        if(hold > 0){
                            lastTopValue = Math.max(lastTopValue, stockInfoList.get(day).getClosePrice());
                        }
                    }
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("totalin", totalin);//总收入
                    map.put("totalout", totalout);//总投入
                    map.put("times", times);//交易次数
                    map.put("hold", hold);//持有数量
                    map.put("holdPrice", hold * stockInfoList.get(stockInfoList.size() - 1).getClosePrice());//持有价值
                    map.put("holdvalue", holdvalue);//持有投入
                    map.put("zhouqi", zhouqi);//周期
                    map.put("sellpoint", sellpoint);//卖点
                    map.put("minday", Utils.min(days));
                    map.put("maxday", Utils.max(days));
                    map.put("averageday", Utils.average(days));
                    map.put("recentDrop", recentDrop);
                    result.add(map);
                    System.out.println(zhouqi + "," + sellpoint);
                }
            }
        }

        BufferedWriter writer = Files.newWriter(new File("D:\\projects\\mine\\"+code+"-"+start+end+".2.csv"), Charsets.UTF_8);
        Joiner j = Joiner.on(",");
        for(Map<String,Object> map : result){
            String s = j.join(map.get("totalin")
                    , map.get("totalout")
                    , map.get("times")
                    , map.get("hold")
                    , map.get("holdPrice")
                    , map.get("holdvalue")
                    , map.get("zhouqi")
                    , map.get("sellpoint")
                    , (Double)map.get("totalin") + (Double) map.get("holdPrice") - (Double)map.get("totalout")
                    , (Double)map.get("totalout") > 0 ? ((Double)map.get("totalin") + (Double) map.get("holdPrice")) / (Double)map.get("totalout") : 0
                    , map.get("minday")
                    , map.get("maxday")
                    , map.get("averageday")
                    , map.get("recentDrop")
            );
            writer.write(s);
            writer.newLine();
        }
        writer.close();

        System.out.println(code);
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
