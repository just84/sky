package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/27.
 */
public class QuadraSentenceWithSingle extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        int size = LocalTools.getSizeOfCards(cards);
        if(!(size % 6 == 0 && size / 6 >= 2)){
            return false;
        }
        if(cards.get(CardModule.CARD_2) == 4){
            return false;
        }
        boolean start = false;
        boolean end = false;
        CardModule startCard = null;
        CardModule endCard = null;
        List<CardModule> attachCards = Lists.newArrayList();
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 4 && cardSize != 1 && cardSize != 0){
                return false;
            }
            if(cardSize == 4 && !start){
                start = true;
                startCard = card;
            }
            if(cardSize != 4 && start && !end){
                end = true;
                endCard = card;
            }
            if(cardSize == 1){
                attachCards.add(card);
            }
        }
        if(startCard != null && endCard != null && attachCards.size() == 2 * (endCard.getValue() - startCard.getValue()) &&
                (endCard.getValue() - startCard.getValue()) == size / 6){
            setCardInfo(startCard, 4, size / 6, attachCards, 1, size / 3);
            return true;
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_K.getValue();
    }
}
