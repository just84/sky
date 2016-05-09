package games.landlords;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by yibin on 16/4/26.
 */
public class Cards {
    Map<CardModule, Integer> cards;

    private Cards(){
        cards = Maps.newHashMap();
    }

    public Integer get(CardModule cardModule){
        return cards.get(cardModule);
    }

    public void put(CardModule cardModule, Integer number){
        if(number < 0 || number > 4){
            throw new IllegalArgumentException("number is "+number);
        }
        if((cardModule.equals(CardModule.CARD_BLACK_KING) || cardModule.equals(CardModule.CARD_RED_KING)) && number > 1){
            throw new IllegalArgumentException("number of King is "+number);
        }

        cards.put(cardModule,number);
    }

    public Set<CardModule> keySet(){
        return cards.keySet();
    }

    public Collection<Integer> values() {
        return cards.values();
    }

    public Cards copy(){
        Cards newCards = new Cards();
        for (CardModule card : CardModule.values()) {
            newCards.put(card, cards.get(card));
        }
        return newCards;
    }

    public static Cards newEmptyCards() {
        Cards newCards = new Cards();
        for (CardModule card : CardModule.values()) {
            newCards.put(card, 0);
        }
        return newCards;
    }

    public static Cards newFullCards() {
        Cards newCards = new Cards();
        for (CardModule card : CardModule.values()) {
            newCards.put(card, 4);
        }
        newCards.put(CardModule.CARD_BLACK_KING, 1);
        newCards.put(CardModule.CARD_RED_KING, 1);
        return newCards;
    }

    public static Cards newCards(String s){
        if(!s.matches("([2-9]|0|J|Q|K|A|B|R)*")){
            throw new IllegalArgumentException("不合法的牌: "+s);
        }
        Cards newCards = newEmptyCards();
        for(char c : s.toCharArray()){
            CardModule cardModule = CardModule.getCardByName(c);
            newCards.put(cardModule,newCards.get(cardModule) + 1);
        }
        return newCards;
    }

    @Override
    public String toString() {
        String result = "";
        if (cards == null) {
            return "";
        }
        for (CardModule card : CardModule.values()) {
            for (int i = 0; i < cards.get(card); i++) {
                result = card.getName() + result;
            }
        }
        return result;
    }
}
