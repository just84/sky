package use.forecast.stock;

import api.Processor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import utils.Utils;

import java.util.List;

/**
 * Created by yibin on 16/3/31.
 */
public class CalculateService {

    private static void calculateBestJudge(List<StockInfo> data) {
        for (int index = data.size() - Utils.max(Constant.RANGES); index > 0; index--) {
            calculateBestJudge(data, index);
        }
    }

    private static void calculateBestJudge(List<StockInfo> data, int index) {
        for (int range : Constant.RANGES) {
            List<Integer> similarities = findSimilarities(index, range, data);
            adjustJudge(data, index, range, similarities);
        }
    }

    private static List<Integer> findSimilarities(Integer begin, Integer range, List<StockInfo> stockInfoList) {
        List<Integer> result = Lists.newArrayList();
//        Double like = stockInfoList.get(begin).getJudgeMap().get(range).getLike();
//        for (int index = begin + range; index > stockInfoList.size() - range; index++) {
//            if (calculatePercentSimilarity(stockInfoList, begin, index, range) > like) {
//                result.add(index);
//            }
//        }
        return result;
    }

    private static void adjustJudge(List<StockInfo> data, int index, int range, List<Integer> similarities) {

    }

    public static Double calculatePercentSimilarity(List<StockInfo> stockInfoList, Integer beginM, Integer beginN, Integer range) {
        Preconditions.checkArgument(stockInfoList != null && range > 0);
        assert stockInfoList != null;
        Double sum = 0d;
        Double sumM = 0d;
        Double sumN = 0d;
        for (int i = 0; i < range; i++) {
            Double M = stockInfoList.get(beginM + i).getPercent();
            Double N = stockInfoList.get(beginN + i).getPercent();
            sum += Math.pow(M - N, 2);
            sumM += Math.pow(M, 2);
            sumN += Math.pow(N, 2);
        }
        if (sumM == 0 && sumN == 0) {
            return 1d;
        }
        return 1 - sum / (sumM + sumN);
    }

    public static Double calculateChangePercent(List<StockInfo> stockInfoList, Integer beginM, Integer beginN, Integer range){
        Double changePercentM = (stockInfoList.get(beginM - range).getClosePrice() - stockInfoList.get(beginM).getClosePrice()) / stockInfoList.get(beginM).getClosePrice();
        Double changePercentN = (stockInfoList.get(beginN - range).getClosePrice() - stockInfoList.get(beginN).getClosePrice()) / stockInfoList.get(beginN).getClosePrice();
        return 2 * changePercentM * changePercentN / (Math.pow(changePercentM,2) + Math.pow(changePercentN,2));
    }

    public static Double calculatePearsonSimilarity(List<StockInfo> stockInfoList, Integer beginM, Integer beginN, Integer range,  Processor<StockInfo,Double> getter) {
        // Pearson Correlation Coefficient
        Preconditions.checkArgument(stockInfoList != null && range > 0);
        assert stockInfoList != null;
        Double averageM = average(stockInfoList, beginM, range, getter);
        Double averageN = average(stockInfoList, beginN, range, getter);
        Double sum = 0d;
        Double sumM = 0d;
        Double sumN = 0d;
        for (int i = 0; i < range; i++) {
            Double M = getter.process(stockInfoList.get(beginM + i)) - averageM;
            Double N = getter.process(stockInfoList.get(beginN + i)) - averageN;
            sum += M * N;
            sumM += Math.pow(M,2);
            sumN += Math.pow(N,2);
        }
        if (sumM == 0 || sumN == 0) {
            if (sumM == 0 && sumN == 0) {
                return 1d;
            }
            return 0d;
        }
        return sum / (Math.sqrt(sumM) * Math.sqrt(sumN));
    }

    public static Double calculateCosSimilarity(List<StockInfo> stockInfoList, Integer beginM, Integer beginN, Integer range, Processor<StockInfo,Double> getter){
        //余弦相似度
        Preconditions.checkArgument(stockInfoList != null && range > 0);
        assert stockInfoList != null;
        Double sum = 0d;
        Double sumM = 0d;
        Double sumN = 0d;

        for (int i = 0; i < range; i++) {
            Double M = getter.process(stockInfoList.get(beginM + i));
            Double N = getter.process(stockInfoList.get(beginN + i));
            sum += M * N;
            sumM += Math.pow(M,2);
            sumN += Math.pow(N,2);
        }
        if (sumM == 0 || sumN == 0) {
            if (sumM == 0 && sumN == 0) {
                return 1d;
            }
            return 0d;
        }
        return sum / (Math.sqrt(sumM) * Math.sqrt(sumN));
    }

    private static Double average(List<StockInfo> data, Integer begin, Integer size, Processor<StockInfo, Double> getter) {
        Preconditions.checkArgument(data != null && begin > 0 && size > 0);
        assert data != null;
        Double sum = 0d;
        for (int i = begin; i < size; i++) {
            sum += getter.process(data.get(i));
        }
        return sum / size;
    }
}
