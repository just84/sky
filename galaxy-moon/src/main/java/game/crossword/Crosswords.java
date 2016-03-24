package game.crossword;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;

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

    public Crosswords() {
        init();
    }

    public void showTable() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row,column).getValue() + " ");
            }
            System.out.print("\n");
        }
    }

    public void showPart() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row, column).getPart() + " ");
            }
            System.out.print("\n");
        }
    }

    public void showPossibility() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(table.findNode(row,column).getPossibleValue().size() + " ");
            }
            System.out.print("\n");
        }
    }

    public void init() {
        remain = 81;
        table = new Table();
        initParts();
        for(int index = 0;index < 9;index++){
            for (Map.Entry<Integer,Integer> set:parts.get(index).getLocation()){
                table.putNode(set.getKey(), set.getValue(), new Node(set.getKey(),set.getValue(),index));
            }
        }
    }

    public void input(String s) {
        if (!s.matches("([1-9] ){2}[1-9]")) {
            System.out.println("格式不正确");
            return;
        }
        String[] input = s.split(" ");
        input(Integer.valueOf(input[0]) - 1, Integer.valueOf(input[1]) - 1, Integer.valueOf(input[2]));
    }

    public void input(int row, int column, int value){
        confirm(table.findNode(row,column), value);
    }

    public void inputLines(List<String> lines) {
        if (lines.size() != 9) {
            System.out.println("行数不正确");
            return;
        }
        for (int row = 0; row < 9; row++) {
            if (!lines.get(row).matches("([0-9] ){8}[0-9]")) {
                System.out.println("格式不正确");
                return;
            }
            String[] line = lines.get(row).split(" ");
            for (int column = 0; column < 9; column++) {
                if ("0".equals(line[column])) {
                    continue;
                }
                input(row, column, Integer.valueOf(line[column]));
            }
        }
    }

    public boolean calculate(){
        exactCalculate();
        showTable();
        if(!guessCalculate(0)){
            logger.info("no answer");
            return false;
        }
        checkTable();
        return true;
    }

    private boolean guessCalculate(int i) {
        if(remain == 0){
            return true;
        }
        logger.info("start to guess");
        Node node = findMinPossibleNode();
        if(node == null){
            return false;
        }
        int oldRemain = remain;
        String oldTable = JsonUtils.serialize(table);
        int oldNodeRow = node.getRow();
        int oldNodeColumn = node.getColumn();
        for(Integer value : Sets.newHashSet(node.getPossibleValue())){
            boolean success = true;
            try{
                confirm(node,value);
                exactCalculate();
                checkTable();
            }catch (Exception e){
                System.out.println(i+"========"+oldRemain+"|"+remain+"failed========");
                showTable();
                success = false;
            }
            if(success){
                System.out.println(i+"========"+oldRemain+"|"+remain+"success========");
                showTable();
                success = guessCalculate(i+1);
                if(success){
                    return true;
                }
            }
            table = JsonUtils.deSerialize(oldTable,Table.class);
            node = table.findNode(oldNodeRow,oldNodeColumn);
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
            for(int j = 0; j < 9; j++) {
                value = table.findNode(i,j).getValue();
                if(value != 0){
                    if(rowSet.contains(value)){
                        logger.error("conflict");
                        throw new RuntimeException("conflict");
                    }
                    rowSet.add(value);
                }

                value = table.findNode(j,i).getValue();
                if(value != 0){
                    if(columnSet.contains(value)){
                        logger.error("conflict");
                        throw new RuntimeException("conflict");
                    }
                    columnSet.add(value);
                }
            }
            rowSet.clear();
            columnSet.clear();
        }
        for(Part part : parts){
            for (Map.Entry<Integer, Integer> location : part.getLocation()) {
                value = table.findNode(location.getKey(),location.getValue()).getValue();
                if(value != 0){
                    if(partSet.contains(value)){
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
        do{
            lastRemain = remain;
            for(Part part : parts){
                for(Integer i : Sets.newHashSet(part.getRemain())){
                    Node possibleNode = null;
                    int times = 0;
                    for(Map.Entry<Integer,Integer> location : part.getLocation()){
                        Node node = table.findNode(location.getKey(), location.getValue());
                        if(node.getPossibleValue().contains(i)){
                            possibleNode = node;
                            times++;
                        }
                    }
//                    if(times == 0){
//                        logger.error("found impossible number:{}, part:{}", i, JsonUtils.serialize(part));
//                        showTable();
//                        throw new RuntimeException("conflict number");
//                    }
                    if(times == 1){
                        confirm(possibleNode,i);
                    }
                }
            }
        }while(lastRemain != remain);
    }

    private void deleteImpossibleNumber(Node node){
        if(node.getValue() == 0){
            return;
        }
        for(int i = 0; i<9;i++){
            deleteAndConfirm(table.findNode(i, node.getColumn()), node);
            deleteAndConfirm(table.findNode(node.getRow(), i), node);
        }
        for(Map.Entry<Integer,Integer> location:parts.get(node.getPart()).getLocation()){
            deleteAndConfirm(table.findNode(location.getKey(), location.getValue()), node);
        }
    }

    private void deleteAndConfirm(Node node, Node baseNode) {
        node.getPossibleValue().remove(baseNode.getValue());
        if(node.getPossibleValue().size() == 1){
            confirm(node, node.getPossibleValue().iterator().next());
        }
    }

    private void handleEveryNode(ElementHandler handler) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                handler.handle(row, column);
            }
        }
    }

    private void confirm(Node node, int value) {
        if(node == null){
            return;
        }
//        if(!node.getPossibleValue().contains(value)){
//            logger.error("found impossible number:{}, node:{}", value, JsonUtils.serialize(node));
//            showTable();
//            throw new RuntimeException("conflict number");
//        }
        node.setValue(value);
        node.getPossibleValue().clear();
        parts.get(node.getPart()).getRemain().remove(value);
        remain--;
        deleteImpossibleNumber(node);
    }

    private void initParts() {
        parts = Lists.newArrayList();
        initPart("0 0,0 1,0 2,1 0,1 1,1 2,2 0,2 1,2 2");
        initPart("0 3,0 4,0 5,1 3,1 4,1 5,2 3,2 4,2 5");
        initPart("0 6,0 7,0 8,1 6,1 7,1 8,2 6,2 7,2 8");
        initPart("3 0,3 1,3 2,4 0,4 1,4 2,5 0,5 1,5 2");
        initPart("3 3,3 4,3 5,4 3,4 4,4 5,5 3,5 4,5 5");
        initPart("3 6,3 7,3 8,4 6,4 7,4 8,5 6,5 7,5 8");
        initPart("6 0,6 1,6 2,7 0,7 1,7 2,8 0,8 1,8 2");
        initPart("6 3,6 4,6 5,7 3,7 4,7 5,8 3,8 4,8 5");
        initPart("6 6,6 7,6 8,7 6,7 7,7 8,8 6,8 7,8 8");
    }

    private void initPart(String s) {
        if(!s.matches("([0-8] [0-8],){8}([0-8] [0-8])")){
            System.out.println("区域格式错误");
            return;
        }
        String[] nodes = s.split(",");
        Set<Map.Entry<Integer,Integer>> set = Sets.newHashSet();
        for (String node : nodes) {
            String[] entry = node.split(" ");
            set.add(new AbstractMap.SimpleEntry<Integer, Integer>(Integer.valueOf(entry[0]), Integer.valueOf(entry[1])));
        }
        Part part = new Part();
        part.setLocation(set);
        parts.add(part);
    }
}
