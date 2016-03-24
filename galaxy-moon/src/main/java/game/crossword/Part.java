package game.crossword;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yibin on 16/3/22.
 */
public class Part {
    private Set<Map.Entry<Integer,Integer>> location;
    private Set<Integer> remain;

    public Part(){
        location = Sets.newHashSet();
        remain = Sets.newHashSet(1,2,3,4,5,6,7,8,9);
    }

    public Set<Map.Entry<Integer, Integer>> getLocation() {
        return location;
    }

    public void setLocation(Set<Map.Entry<Integer, Integer>> location) {
        this.location = location;
    }

    public Set<Integer> getRemain() {
        return remain;
    }

    public void setRemain(Set<Integer> remain) {
        this.remain = remain;
    }
}
