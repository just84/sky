package games.game2048;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by yibin on 2016/11/8.
 * 0 1 2 3
 * 4 5 6 7
 * 8 9 a b
 * c d e f
 */
public class Play2048 {
    private static List<Integer> data = Lists.newArrayListWithExpectedSize(16);

    static {
        for (int i = 0; i < 16; i++) {
            data.add(i, 0);
        }
    }

    public static void up() {
        int end;
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
                    } else {
                        target = k + 1;
                    }
                    if (target != j) {
                        data.set(i + 4 * target, data.get(i + 4 * target) + data.get(i + 4 * j));
                        data.set(i + 4 * j, 0);
                    }
                }
            }
        }
    }

    public static void down() {

    }

    public static void left() {

    }

    public static void right() {

    }

    public static List<Integer> getData() {
        return data;
    }

    public static void setData(List<Integer> data) {
        Play2048.data = data;
    }

    public static void showData() {
        Joiner joiner = Joiner.on(" ");
        System.out.println(joiner.join(data.get(0), data.get(1), data.get(2), data.get(3)));
        System.out.println(joiner.join(data.get(4), data.get(5), data.get(6), data.get(7)));
        System.out.println(joiner.join(data.get(8), data.get(9), data.get(10), data.get(11)));
        System.out.println(joiner.join(data.get(12), data.get(13), data.get(14), data.get(15)));
    }
}
