package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class TripleSentenceWithDouble extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        int size = LocalTools.getSizeOfCards(cards);
        if(!(size % 5 == 0 && size / 5 >= 2)){
            return false;
        }
        if(cards.get(CardModule.CARD_2) == 3){
            return false;
        }
        boolean start = false;
        boolean end = false;
        CardModule startCard = null;
        CardModule endCard = null;
        List<CardModule> attachCards = Lists.newArrayList();
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 3 && cardSize != 2 && cardSize != 0){
                return false;
            }
            if(cardSize == 3 && !start){
                start = true;
                startCard = card;
            }
            if(cardSize != 3 && start && !end){
                end = true;
                endCard = card;
            }
            if(cardSize == 2){
                attachCards.add(card);
            }
        }
        if(startCard != null && endCard != null && attachCards.size() == size / 5 &&
                (endCard.getValue() - startCard.getValue()) == size / 5){
            setCardInfo(startCard, 3, size / 5, attachCards, 2, size / 5);
            return true;
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_K.getValue();
    }
}
