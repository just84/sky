package games.landlords;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by yibin on 16/4/26.
 */
public class LocalTools {

    public static Cards splitCards(String cards) {
        Cards result = Cards.newEmptyCards();
        for (Character c : cards.toCharArray()) {
            CardModule card = CardModule.getCardByName(c);
            result.put(card, result.get(card) + 1);
        }
        return result;
    }

    public static Cards addCards(Cards base, Cards addition) {
        if (addition == null) {
            return base;
        }
        for (CardModule card : base.keySet()) {
            base.put(card, base.get(card) + addition.get(card));
        }
        return base;
    }

    public static Cards subCards(Cards base, Cards subtraction) {
        if (subtraction == null) {
            return base;
        }
        for (CardModule card : base.keySet()) {
            base.put(card, base.get(card) - subtraction.get(card));
        }
        return base;
    }

    public static Integer getNumberOfCards(Cards cards) {
        if(cards == null){
            return 0;
        }
        Integer size = 0;
        for (Integer i : cards.values()) {
            size += i;
        }
        return size;
    }
}
