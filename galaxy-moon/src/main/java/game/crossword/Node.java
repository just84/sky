package game.crossword;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by yibin on 16/3/22.
 */
public class Node {
    private int value;
    private int row;
    private int column;
    private List<Integer> parts;
    private Set<Integer> possibleValue;

    public Node(){

    }

    public Node(int row, int column, int part){
        value = 0;
        this.row = row;
        this.column = column;
        parts = Lists.newArrayList(part);
        possibleValue = Sets.newHashSet(1,2,3,4,5,6,7,8,9);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public List<Integer> getParts() {
        return parts;
    }

    public void setPart(List<Integer> parts) {
        this.parts = parts;
    }

    public Set<Integer> getPossibleValue() {
        return possibleValue;
    }

    public void setPossibleValue(Set<Integer> possibleValue) {
        this.possibleValue = possibleValue;
    }
}
