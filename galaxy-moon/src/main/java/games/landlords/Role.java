package games.landlords;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by yibin on 16/4/26.
 */
public enum Role {
    landlord(0),//地主
    nPlayer(1),//地主下家
    pPlayer(2);//地主上家

    private int index;
    private static Map<Integer,Role> integerRoleMap = Maps.newHashMap();

    static {
        integerRoleMap.put(0,landlord);
        integerRoleMap.put(1,nPlayer);
        integerRoleMap.put(2,pPlayer);
    }

    Role(int i){
        index = i;
    }

    public static Role getRoleByIndex(int index){
        return integerRoleMap.get(index);
    }

    public int getIndex() {
        return index;
    }
}
