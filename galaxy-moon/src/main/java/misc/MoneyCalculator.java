package misc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 2016/10/21.
 */
public class MoneyCalculator {

    public static double debj(double bj, double p, int mouth, int reinvest) {
        return debj(bj,p,mouth,reinvest,0);
    }
    /**
     * 等额本金还款计算
     *
     * @param bj       本金
     * @param p        月利率
     * @param mouth    借款月数
     * @param reinvest 复投次数
     * @return 全部还清时的总金额
     */
    private static double debj(double bj, double p, int mouth, int reinvest, int deep) {
        if (reinvest <= deep) {
            return bj;
        }
        double bjPerMouth = bj / mouth;
        double leftBj = bj;
        double total = 0;
        for (int i = 0; i < mouth; i++) {
            double lixi = leftBj * p;
            double receivedMoney = bjPerMouth + lixi;
            leftBj = leftBj - bjPerMouth;
            double back = debj(receivedMoney, p, mouth, reinvest, deep+1+i);
            total += back;
//            if(deep < 1)
//            printBack(deep,back);
        }
        return total;
    }

    public static double debx(double bj, double p, int mouth, int reinvest) {
        return debx(bj,p,mouth,reinvest,0);
    }

    /**
     * 等额本息还款计算
     *
     * @param bj       本金
     * @param p        月利率
     * @param mouth    借款月数
     * @param reinvest 复投次数
     * @return 全部还清时的总金额
     */
    public static double debx(double bj, double p, int mouth, int reinvest, int deep) {
        if (reinvest <= deep) {
            return bj;
        }
        double receivedPerMouth = getDebxBackMoneyPerMouth(bj,p,mouth);
        double total = 0;
        for (int i = 0; i < mouth; i++) {
            double back = debx(receivedPerMouth, p, mouth, reinvest, deep+1+i);
            total += back;
//            if(deep < 1)
//            printBack(deep,back);
        }
        return total;
    }

    private static void printBack(int deep, double back) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<deep;i++){
            sb.append("  ");
        }
        sb.append((int)back);
        System.out.println(sb.toString());
    }

    public static double debjByMouth(double bj, double addition, double p, int length){
        double total = bj;
        for(int i=0;i<length;i++){
            total += total * p;
            total += addition;
        }
        return total;
    }

    public static double debxByMouth(double bj, double addition, double p, int mouth, int length){
        List<BackBJ> backBJList = Lists.newArrayListWithExpectedSize(mouth);
        backBJList.add(new BackBJ(getDebxBackMoneyPerMouth(bj, p, mouth),mouth));
        for(int i=0;i<length;i++){
            double receivedMoney = getReceivedMoney(backBJList);
            receivedMoney += addition;
            backBJList.add(new BackBJ(getDebxBackMoneyPerMouth(receivedMoney,p,mouth),mouth));
        }
        return outputBackBJList(backBJList,mouth);
    }

    private static double outputBackBJList(List<BackBJ> backBJList,int mouth) {
        double total = 0;
        for(int i=0;i<mouth;i++){
            total *= (1.004);
            double back = getReceivedMoney(backBJList);
            total += back;
            System.out.println(back);
        }
        System.out.println("total:"+total);
        return total;
    }

    private static double getReceivedMoney(List<BackBJ> backBJList) {
        double backTotal = 0;
        List<BackBJ> needRemove = Lists.newArrayList();
        for(BackBJ backBJ: backBJList){
            backTotal += backBJ.getBackBjPerMouth();
            backBJ.countDownMouth();
            if(backBJ.getLeftMouth() == 0){
                needRemove.add(backBJ);
            }
        }
        backBJList.removeAll(needRemove);
        return backTotal;
    }

    private static double getDebxBackMoneyPerMouth(double bj, double p, int mouth){
        //每月还款额=[贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1]
        double temp = Math.pow(1 + p, mouth);
        return bj * p * temp / (temp - 1);
    }

    private static class BackBJ{
        private double backBjPerMouth;
        private int leftMouth;

        public BackBJ(double backBjPerMouth, int leftMouth) {
            this.backBjPerMouth = backBjPerMouth;
            this.leftMouth = leftMouth;
        }

        public double getBackBjPerMouth() {
            return backBjPerMouth;
        }

        public void setBackBjPerMouth(double backBjPerMouth) {
            this.backBjPerMouth = backBjPerMouth;
        }

        public int getLeftMouth() {
            return leftMouth;
        }

        public void setLeftMouth(int leftMouth) {
            this.leftMouth = leftMouth;
        }

        public void countDownMouth(){
            leftMouth--;
        }
    }
}
