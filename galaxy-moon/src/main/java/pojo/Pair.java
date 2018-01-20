package pojo;

import java.io.Serializable;

/**
 * Created by yibin on 2016/11/29.
 */
public final class Pair<F,S> implements Serializable {
    public F first;
    public S second;

    public Pair() {
    }

    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }

    public static <F, S> Pair<F, S> makePair(F f, S s) {
        return new Pair(f, s);
    }

    private static <T> boolean eq(T o1, T o2) {
        return o1 == null?o2 == null:o1.equals(o2);
    }

    public boolean equals(Object o) {
        Pair pr = (Pair)o;
        return pr == null?false:eq(this.first, pr.first) && eq(this.second, pr.second);
    }

    private static int h(Object o) {
        return o == null?0:o.hashCode();
    }

    public int hashCode() {
        int seed = h(this.first);
        seed ^= h(this.second) + -1640531527 + (seed << 6) + (seed >> 2);
        return seed;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(this.first).append(", ").append(this.second).append("}");
        return sb.toString();
    }
}
