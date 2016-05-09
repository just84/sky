package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class DoubleSentence extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        int size = LocalTools.getSizeOfCards(cards);
        if(!(size % 2 == 0 && size / 2 >= 3)){
            return false;
        }
        if(cards.get(CardModule.CARD_2) == 2){
            return false;
        }
        boolean start = false;
        CardModule startCard = null;
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 2 && cardSize != 0){
                return false;
            }
            if(cardSize == 2 && !start){
                start = true;
                startCard = card;
            }
            if(cardSize != 2 && start){
                if(card.getValue() - startCard.getValue() == size / 2){
                    setCardInfo(startCard, 2, size / 2);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_Q.getValue();
    }
}
