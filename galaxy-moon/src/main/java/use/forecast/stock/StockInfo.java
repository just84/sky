package use.forecast.stock;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Created by yibin on 16/3/30.
 */
public class StockInfo {
    private String market;
    private String code;
    private Date date;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Long volume;
    private Double adjClosePrice;
    private Double percent;

    //key:检查天数n,value:最优的judge
    private Map<Integer,Judge> judgeMap;

    public StockInfo(){}

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Double getAdjClosePrice() {
        return adjClosePrice;
    }

    public void setAdjClosePrice(Double adjClosePrice) {
        this.adjClosePrice = adjClosePrice;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Map<Integer, Judge> getJudgeMap() {
        return judgeMap;
    }

    public void setJudgeMap(Map<Integer, Judge> judgeMap) {
        this.judgeMap = judgeMap;
    }
}
