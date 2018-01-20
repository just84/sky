package games.game2048;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import pojo.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yibin on 2016/11/8.
 * 0 1 2 3
 * 4 5 6 7
 * 8 9 a b
 * c d e f
 */
public class Play2048 {
    private static List<Integer> baseData = Lists.newArrayListWithExpectedSize(16);
    private static int space;

    static {
        for (int i = 0; i < 16; i++) {
            baseData.add(i, 0);
        }
        space = 16;
    }

    public static void run() throws Exception {
        while(space>0){
            Thread.sleep(500);
            showData();
            produceRandomNumber();
            Thread.sleep(500);
            showData();
            action();
        }
    }

    public static void runAndWriteFile(File file) throws Exception {
        BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
        while(space>0){
            writeData(writer);
            produceRandomNumber();
            writeData(writer);
            action();
        }
        writer.close();
    }

    private static void action(){
        if(up() > 0){
            return;
        }
        if(left() > 0){
            return;
        }
        if(right() > 0){
            return;
        }
        down();
    }

    private static Pair<Double,Double> calculate(List<Integer> data){
        return Pair.makePair(0d,0d);
    }

    private static List<Integer> tryMove(char action){
        List<Integer> oldData = Lists.newArrayList(baseData);
        switch (action){
            case 'u' : up();break;
            case 'd' : down();break;
            case 'l' : left();break;
            case 'r' : right();break;
        }
        List<Integer> result = baseData;
        baseData = oldData;
        return result;
    }

    private static int up() {
        return doMove(baseData);
    }

    private static int down() {
        return doMove(Lists.reverse(baseData));
    }

    private static int left() {
        transToLeft(baseData);//沿左上-右下做对称变换
        int moved = doMove(baseData);
        transToLeft(baseData);//恢复对称变换
        return moved;
    }

    private static int right() {
        transToLeft(baseData);//沿左上-右下做对称变换
        int moved = doMove(Lists.reverse(baseData));
        transToLeft(baseData);//恢复对称变换
        return moved;
    }

    /**
     * 向上移动
     */
    private static int doMove(List<Integer> data){
        int end;
        int moved = 0;
        for (int i = 0; i < 4; i++) {
            end = 0;
            for (int j = 0; j < 4; j++) {
                if (data.get(i + 4 * j) != 0) {
                    int k, target;
                    for (k = j - 1; k >= end; k--) {
                        if (data.get(i + 4 * k) != 0) {
                            break;
                        }
                    }
                    if (k >= end && data.get(i + 4 * k).equals(data.get(i + 4 * j))) {
                        target = k;
                        end = k + 1;
                        space++;
                    } else {
                        target = k + 1;
                    }
                    if (target != j) {
                        data.set(i + 4 * target, data.get(i + 4 * target) + data.get(i + 4 * j));
                        data.set(i + 4 * j, 0);
                        moved ++;
                    }
                }
            }
        }
        return moved;
    }

    /**
     * 对4*4矩阵沿左上-右下对角线做对称变换
     */
    private static void transToLeft(List<Integer> data) {
        for(int i = 0;i < 4;i++){
            for(int j = 0;j < i;j++){
                swap(data,i+4*j,4*i+j);
            }
        }
    }

    private static <T> void swap(List<T> l, int a, int b){
        T t = l.get(a);
        l.set(a,l.get(b));
        l.set(b,t);
    }

    /**
     * 在空位随机产生一个2/4，概率分别是0.7/0.3
     */
    private static void produceRandomNumber(){
        int number = Math.random() < 0.7 ? 2 : 4;
        int index = (int)Math.floor(space * Math.random()) % space;
        for(int i = 0; i< 16;i++){
            if(baseData.get(i) == 0){
                if(index == 0){
                    baseData.set(i,number);
                    space--;
                    break;
                } else {
                    index--;
                }
            }
        }
    }

    private static void showData() {
        Joiner joiner = Joiner.on(" ");
        System.out.println(joiner.join(baseData.get(0), baseData.get(1), baseData.get(2), baseData.get(3)));
        System.out.println(joiner.join(baseData.get(4), baseData.get(5), baseData.get(6), baseData.get(7)));
        System.out.println(joiner.join(baseData.get(8), baseData.get(9), baseData.get(10), baseData.get(11)));
        System.out.println(joiner.join(baseData.get(12), baseData.get(13), baseData.get(14), baseData.get(15)));
        System.out.println("space:" + space);
    }

    private static void writeData(BufferedWriter writer) throws IOException {
        Joiner joiner = Joiner.on(" ");
        writer.write(joiner.join(baseData.get(0), baseData.get(1), baseData.get(2), baseData.get(3)));
        writer.newLine();
        writer.write(joiner.join(baseData.get(4), baseData.get(5), baseData.get(6), baseData.get(7)));
        writer.newLine();
        writer.write(joiner.join(baseData.get(8), baseData.get(9), baseData.get(10), baseData.get(11)));
        writer.newLine();
        writer.write(joiner.join(baseData.get(12), baseData.get(13), baseData.get(14), baseData.get(15)));
        writer.newLine();
        writer.write("space:" + space);
        writer.newLine();
    }
}
