package game.crossword;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Utils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yibin on 16/3/22.
 */
public class Crosswords {
    private static final Logger logger = LoggerFactory.getLogger(Crosswords.class);
    private Table table;
    private List<Part> parts;
    private int remain;
    private static final String DEFAULT_PARTITION =
            "0 0 0 1 1 1 2 2 2;" +
            "0 0 0 1 1 1 2 2 2;" +
            "0 0 0 1 1 1 2 2 2;" +
            "3 3 3 4 4 4 5 5 5;" +
            "3 3 3 4 4 4 5 5 5;" +
            "3 3 3 4 4 4 5 5 5;" +
            "6 6 6 7 7 7 8 8 8;" +
            "6 6 6 7 7 7 8 8 8;" +
            "6 6 6 7 7 7 8 8 8;";

    public Crosswords() {
        init(DEFAULT_PARTITION);
    }

    public Crosswords(String partition) {
        init(partition);
    }

    public void showTable() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row, column).getValue() + " ");
            }
            System.out.print("\n");
        }
    }

    public void showPart() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row, column).getParts() + " ");
            }
            System.out.print("\n");
        }
    }

    public void showPossibility() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row, column).getPossibleValue().size() + " ");
            }
            System.out.print("\n");
        }
    }

    public void init(String partition) {
        remain = 81;
        table = new Table();
        initParts(partition);
        for (int index = 0; index < 9; index++) {
            for (Map.Entry<Integer, Integer> set : parts.get(index).getLocation()) {
                table.putNode(set.getKey(), set.getValue(), new Node(set.getKey(), set.getValue(), index));
            }
        }
    }

    public void input(String s) {
        Preconditions.checkArgument(s.matches("([1-9] ){2}[1-9]"), "invalid input");
        List<String> input = Utils.getSplitter(" ").splitToList(s);
        input(Integer.valueOf(input.get(0)) - 1, Integer.valueOf(input.get(1)) - 1, Integer.valueOf(input.get(2)));
    }

    public void input(int row, int column, int value) {
        confirm(table.findNode(row, column), value);
    }

    public void inputLines(List<String> lines) {
        Preconditions.checkArgument(lines.size() == 9, "number of rows invalid");
        for (int row = 0; row < 9; row++) {
            Preconditions.checkArgument(lines.get(row).matches("([0-9] ){8}[0-9]"), String.format("line %d invalid", row));
            List<String> line = Utils.getSplitter(" ").splitToList(lines.get(row));
            for (int column = 0; column < 9; column++) {
                if ("0".equals(line.get(column))) {
                    continue;
                }
                input(row, column, Integer.valueOf(line.get(column)));
            }
        }
    }

    public void addExtraPart(String s) {
        addPart(s);
        int index = parts.size() - 1;
        for (Map.Entry<Integer, Integer> location : parts.get(index).getLocation()) {
            table.findNode(location.getKey(), location.getValue()).getParts().add(index);
        }
    }

    public boolean calculate() {
        exactCalculate();
        showTable();
        if (!guessCalculate(0)) {
            logger.info("no answer");
            return false;
        }
        checkTable();
        return true;
    }

    private boolean guessCalculate(int deep) {
        if (remain == 0) {
            return true;
        }
        logger.info("start to guess, deep:{}", deep);
        Node node = findMinPossibleNode();
        if (node == null) {
            return false;
        }
        int oldRemain = remain;
        String oldTable = JsonUtils.serialize(table);
        int oldNodeRow = node.getRow();
        int oldNodeColumn = node.getColumn();
        for (Integer value : Sets.newHashSet(node.getPossibleValue())) {
            boolean success = true;
            try {
                confirm(node, value);
                exactCalculate();
                checkTable();
            } catch (Exception e) {
                System.out.println(deep + "========" + oldRemain + "|" + remain + "failed========");
                showTable();
                success = false;
            }
            if (success) {
                System.out.println(deep + "========" + oldRemain + "|" + remain + "success========");
                showTable();
                success = guessCalculate(deep + 1);
                if (success) {
                    return true;
                }
            }
            table = JsonUtils.deSerialize(oldTable, Table.class);
            node = table.findNode(oldNodeRow, oldNodeColumn);
            remain = oldRemain;
        }
        return false;
    }

    private void checkTable() {
        Set<Integer> rowSet = Sets.newHashSet();
        Set<Integer> columnSet = Sets.newHashSet();
        Set<Integer> partSet = Sets.newHashSet();
        int value = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                value = table.findNode(i, j).getValue();
                if (value != 0) {
                    if (rowSet.contains(value)) {
                        logger.error("conflict");
                        throw new RuntimeException("conflict");
                    }
                    rowSet.add(value);
                }

                value = table.findNode(j, i).getValue();
                if (value != 0) {
                    if (columnSet.contains(value)) {
                        logger.error("conflict");
                        throw new RuntimeException("conflict");
                    }
                    columnSet.add(value);
                }
            }
            rowSet.clear();
            columnSet.clear();
        }
        for (Part part : parts) {
            for (Map.Entry<Integer, Integer> location : part.getLocation()) {
                value = table.findNode(location.getKey(), location.getValue()).getValue();
                if (value != 0) {
                    if (partSet.contains(value)) {
                        logger.error("conflict");
                        throw new RuntimeException("conflict");
                    }
                    partSet.add(value);
                }
            }
            partSet.clear();
        }
    }

    private Node findMinPossibleNode() {
        int min = Integer.MAX_VALUE;
        Node minNode = null;
        for (Node node : table.getNodes()) {
            if (node.getPossibleValue().size() == 2) {
                return node;
            }
            if (min > node.getPossibleValue().size()) {
                min = node.getPossibleValue().size();
                minNode = node;
            }
        }
        return minNode;
    }

    private void exactCalculate() {
        int lastRemain;
        do {
            lastRemain = remain;
            for (Part part : parts) {
                for (Integer i : Sets.newHashSet(part.getRemain())) {
                    Node possibleNode = null;
                    int times = 0;
                    for (Map.Entry<Integer, Integer> location : part.getLocation()) {
                        Node node = table.findNode(location.getKey(), location.getValue());
                        if (node.getPossibleValue().contains(i)) {
                            possibleNode = node;
                            times++;
                        }
                    }
                    if (times == 1) {
                        confirm(possibleNode, i);
                    }
                }
            }
        } while (lastRemain != remain);
    }

    private void deleteImpossibleNumber(Node node) {
        if (node.getValue() == 0) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            deleteAndConfirm(table.findNode(i, node.getColumn()), node);
            deleteAndConfirm(table.findNode(node.getRow(), i), node);
        }
        for (Integer part : node.getParts()) {
            for (Map.Entry<Integer, Integer> location : parts.get(part).getLocation()) {
                deleteAndConfirm(table.findNode(location.getKey(), location.getValue()), node);
            }
        }
    }

    private void deleteAndConfirm(Node node, Node baseNode) {
        node.getPossibleValue().remove(baseNode.getValue());
        if (node.getPossibleValue().size() == 1) {
            confirm(node, node.getPossibleValue().iterator().next());
        }
    }

    private void confirm(Node node, int value) {
        if (node == null) {
            return;
        }
        node.setValue(value);
        node.getPossibleValue().clear();
        for (Integer part : node.getParts()) {
            parts.get(part).getRemain().remove(value);
        }
        remain--;
        deleteImpossibleNumber(node);
    }

    private void initParts(String partition) {
        List<String> partStrings = Utils.getSplitter(";").splitToList(partition);
        Preconditions.checkArgument(partStrings.size() == 9, "partition size is invalid");
        parts = Lists.newLinkedList(Sets.newHashSet(new Part(), new Part(), new Part(), new Part(), new Part(), new Part(), new Part(), new Part(), new Part()));
        for (int row = 0; row < 9; row++) {
            Preconditions.checkArgument(partStrings.get(row).matches("([0-8] ){8}([0-8])"), "partition is invalid: " + partStrings.get(row));
            List<String> index = Utils.getSplitter(" ").splitToList(partStrings.get(row));
            for (int column = 0; column < 9; column++) {
                parts.get(Integer.valueOf(index.get(column))).getLocation().add(new AbstractMap.SimpleEntry<Integer, Integer>(row, column));
            }
        }
    }

    private void addPart(String s) {
        Preconditions.checkArgument(s.matches("([0-8] [0-8],){8}([0-8] [0-8])"), "partition is invalid: " + s);
        List<String> nodes = Utils.getSplitter(",").splitToList(s);
        Set<Map.Entry<Integer, Integer>> set = Sets.newHashSet();
        for (String node : nodes) {
            List<String> entry = Utils.getSplitter(" ").splitToList(node);
            set.add(new AbstractMap.SimpleEntry<Integer, Integer>(Integer.valueOf(entry.get(0)), Integer.valueOf(entry.get(1))));
        }
        Part part = new Part();
        part.setLocation(set);
        parts.add(part);
    }
}
