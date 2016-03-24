import com.google.common.collect.Lists;
import game.crossword.Crosswords;
import game.crossword.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;

import java.util.List;

/**
 * Created by yibin on 16/3/15.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception{
        Crosswords crosswords = new Crosswords();
        List<String> list = Lists.newArrayList();
        putIn0(list);
        crosswords.inputLines(list);
        crosswords.calculate();
        System.out.println("==================");
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
}


