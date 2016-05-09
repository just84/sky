package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class Sentence extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        int size = LocalTools.getSizeOfCards(cards);
        if(!(size >= 5)){
            return false;
        }
        if(cards.get(CardModule.CARD_2) == 1){
            return false;
        }
        boolean start = false;
        CardModule startCard = null;
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 1 && cardSize != 0){
                return false;
            }
            if(cardSize == 1 && !start){
                start = true;
                startCard = card;
            }
            if(cardSize != 1 && start){
                if(card.getValue() - startCard.getValue() == size){
                    setCardInfo(startCard, 1, size);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_10.getValue();
    }
}
