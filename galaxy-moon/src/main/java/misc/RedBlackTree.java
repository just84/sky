package misc;

/**
 * 红黑树的五个性质：
 1）每个结点要么是红的，要么是黑的。
 2）根结点是黑的。
 3）每个叶结点，即空结点（NIL）是黑的。
 4）如果一个结点是红的，那么它的俩个儿子都是黑的。
 5）对每个结点，从该结点到其子孙结点的所有路径上包含相同数目的黑结点。
 * Created by yibin on 2016/9/30.
 */
public class RedBlackTree<K extends Comparable<? super K>,V> {
    private Node root;

    public V query(K key){
        Node p = root;
        if(key == null || p == null){
            return null;
        }
        while( p != null && key.compareTo(p.key) != 0){
            if(key.compareTo(p.key) > 0){
                p = p.rightChild;
            } else {
                p = p.leftChild;
            }
        }
        return p == null ? null : p.value;
    }

    //好麻烦，暂弃
    public void insert(K key, V value){
        if(root == null){
            root = new Node(key,value,null,null,null,Color.BLACK);
        }
    }

    //同上
    public boolean delete(K key){
        return false;
    }

    private class Node{
        private K key;
        private V value;
        private Node leftChild;
        private Node rightChild;
        private Node parent;
        private Color color;

        Node(K key, V value, Node leftChild, Node rightChild, Node parent, Color color){
            this.key = key;
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.parent = parent;
            this.color = color;
        }

        private Node getGrandParent(){
            if(parent == null){
                return null;
            }
            return parent.parent;
        }

        private Node getUncle(){
            Node grandParent = getGrandParent();
            if (parent == null || grandParent == null){
                return null;
            }
            if(parent == grandParent.leftChild){
                return grandParent.rightChild;
            } else {
                return grandParent.leftChild;
            }
        }
    }

    private enum Color{
        RED,BLACK
    }
}
