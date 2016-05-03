package games.crossword;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by yibin on 16/3/23.
 */
public class Table {
    private List<Node> nodes;

    public Table(){
        nodes = Lists.newLinkedList();
        for(int i =0;i<81;i++){
            nodes.add(null);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Node findNode(int row, int column) {
        return nodes.get(trans(row, column));
    }

    public void putNode(int row, int column, Node node) {
        nodes.set(trans(row, column), node);
    }

    private int trans(int row, int column) {
        return row * 9 + column;
    }
}
