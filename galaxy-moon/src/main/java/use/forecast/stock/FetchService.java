package use.forecast.stock;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;

/**
 * Created by yibin on 16/3/30.
 */
public class FetchService {
    private static final Logger logger = LoggerFactory.getLogger(FetchService.class);
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final String historyDataUrl = "http://stock.liangyee.com/bus-api/stock/freeStockMarketData/getDailyKBar?";

    public static List<StockInfo> fetchHistoryData(String stockCode, String startDate, String endDate) {
        Preconditions.checkArgument(stockCode.matches("[0-9]{6}"));
        Preconditions.checkArgument(startDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"));
        Preconditions.checkArgument(endDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"));

        CloseableHttpResponse httpResponse = null;
        try {
            //"userKey=ED340946E9DF4176866068E630DF7FD5&symbol=600100&startDate=2015-01-01&endDate=2016-02-20"
            StringBuilder sb = new StringBuilder();
            sb.append(historyDataUrl)
                    .append("&userKey=ED340946E9DF4176866068E630DF7FD5")
                    .append("&symbol=").append(stockCode)
                    .append("&startDate=").append(startDate)
                    .append("&endDate=").append(endDate)
                    .append("&type=").append(0);
            httpResponse = httpClient.execute(new HttpGet(sb.toString()));
            Data data = JsonUtils.deSerialize(EntityUtils.toString(httpResponse.getEntity(),Charsets.UTF_8),Data.class);
            List<StockInfo> resultList = Lists.newLinkedList();

            if(data == null || !"000000".equals(data.getCode()) || data.getResult() == null){
                return resultList;
            }
            for(String line : data.getResult()){
                StockInfo stockInfo = makeHistoryStockInfo(line, stockCode);
                if(stockInfo != null){
                    resultList.add(stockInfo);
                }
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeAll(httpResponse);
        }
        return null;
    }

    public static StockInfo fetchNewestData(String stockCode, String market) {
        Preconditions.checkArgument(stockCode.matches("[0-9]{6}"));
        Preconditions.checkArgument(market.matches("ss|sz"));
        String queryCode = market.replace("ss","sh") + stockCode;
        CloseableHttpResponse httpResponse = null;
        BufferedReader reader = null;
        String line = null;
        try {
            httpResponse = httpClient.execute(new HttpGet("http://qt.gtimg.cn/q=" + queryCode));
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8));
            line = reader.readLine();
            return makeNewestStockInfo(line, stockCode, market);
        } catch (IOException e) {
            logger.error("fetch history data failed, stockCode:{}, market:{}", stockCode, market, e);
        } catch (ParseException e) {
            logger.error("parse info failed, line:{}", line, e);
        } finally {
            Utils.closeAll(reader, httpResponse);
        }
        return null;
    }

    private static StockInfo makeHistoryStockInfo(String info, String stockCode) {
        try {
            List<String> data = Utils.getSplitter(",").splitToList(info);
            Preconditions.checkArgument(data.size() == 7);
            if(Long.valueOf(data.get(5)) == 0){
                return null;
            }
            StockInfo stockInfo = new StockInfo();
            stockInfo.setCode(stockCode);
            stockInfo.setDate(DateUtils.parseDate(data.get(0), "yyyy-MM-dd"));
            stockInfo.setOpenPrice(Double.valueOf(data.get(1)));
            stockInfo.setClosePrice(Double.valueOf(data.get(2)));
            stockInfo.setHighPrice(Double.valueOf(data.get(3)));
            stockInfo.setLowPrice(Double.valueOf(data.get(4)));
            stockInfo.setVolume(Long.valueOf(data.get(5)));
            stockInfo.setPercent(Double.valueOf(data.get(6)));
            return stockInfo;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static StockInfo makeNewestStockInfo(String info, String stockCode, String market) throws ParseException {
        List<String> data = Utils.getSplitter("~").splitToList(info);
        Preconditions.checkArgument(data.size() > 35);
        if(Long.valueOf(data.get(6)) == 0){
            return null;
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setMarket(market);
        stockInfo.setCode(stockCode);
        stockInfo.setDate(DateUtils.parseDate(data.get(30), "yyyyMMddHHmmss"));
        stockInfo.setOpenPrice(Double.valueOf(data.get(5)));
        stockInfo.setHighPrice(Double.valueOf(data.get(33)));
        stockInfo.setLowPrice(Double.valueOf(data.get(34)));
        stockInfo.setClosePrice(Double.valueOf(data.get(3)));
        stockInfo.setVolume(Long.valueOf(data.get(6)) * 100);
        stockInfo.setPercent(stockInfo.getClosePrice() / stockInfo.getOpenPrice() - 1);
        return stockInfo;
    }

}
