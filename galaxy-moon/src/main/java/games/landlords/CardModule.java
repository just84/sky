package games.landlords;

import api.Processor;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by yibin on 16/4/25.
 */
public enum CardModule {
    CARD_3('3',0),
    CARD_4('4',1),
    CARD_5('5',2),
    CARD_6('6',3),
    CARD_7('7',4),
    CARD_8('8',5),
    CARD_9('9',6),
    CARD_10('0',7),
    CARD_J('J',8),
    CARD_Q('Q',9),
    CARD_K('K',10),
    CARD_A('A',11),
    CARD_2('2',12),
    CARD_BLACK_KING('B',13),
    CARD_RED_KING('R',14);

    public static final Integer MIN_VALUE = 0;
    public static final Integer MAX_VALUE = 14;
    private Character name;
    private Integer value;
    private static Map<Character, CardModule> nameCardMap = Maps.newHashMap();
    private static Map<Integer, CardModule> valueCardMap = Maps.newHashMap();

    static {
        for(CardModule card : CardModule.values()){
            nameCardMap.put(card.getName(),card);
            valueCardMap.put(card.getValue(),card);
        }
    }

    CardModule(Character name, Integer value){
        this.name = name;
        this.value = value;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static CardModule getCardByName(Character name){
        return nameCardMap.get(name);
    }

    public static CardModule getCardByValue(Integer value){
        return valueCardMap.get(value);
    }
}
