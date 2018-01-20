package pojo;

import java.io.Serializable;

/**
 * Created by yibin on 2016/11/29.
 */
public final class Triple<F, S, T> implements Serializable {
    private F first;
    private S second;
    private T third;

    public Triple() {
    }

    public Triple(F f, S s, T t) {
        this.first = f;
        this.second = s;
        this.third = t;
    }

    public static <F, S, T> Triple<F, S, T> makeTriple(F f, S s, T t) {
        return new Triple(f, s, t);
    }

    private static <T> boolean eq(T o1, T o2) {
        return o1 == null?o2 == null:o1.equals(o2);
    }

    public boolean equals(Object o) {
        Triple pr = (Triple)o;
        return pr == null?false:eq(this.first, pr.first) && eq(this.second, pr.second) && eq(this.third, pr.third);
    }

    private static int h(Object o) {
        return o == null?0:o.hashCode();
    }

    public int hashCode() {
        int seed = h(this.first);
        seed ^= h(this.second) + -1640531527 + (seed << 6) + (seed >> 2);
        seed ^= h(this.third) + -1640531527 + (seed << 6) + (seed >> 2);
        return seed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(this.first).append(", ").append(this.second).append(", ").append(this.third).append("}");
        return sb.toString();
    }

    public F getFirst() {
        return this.first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return this.second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public T getThird() {
        return this.third;
    }

    public void setThird(T third) {
        this.third = third;
    }
}