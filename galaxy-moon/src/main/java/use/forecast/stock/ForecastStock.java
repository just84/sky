package use.forecast.stock;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by yibin on 16/3/30.
 */
public class ForecastStock {

    public static void forecast(String code, String market) {
        List<StockInfo> stockInfoList = FetchService.fetchHistoryData(code, market);
        StockInfo recentStockInfo = FetchService.fetchNewestData(code, market);
    }
}
