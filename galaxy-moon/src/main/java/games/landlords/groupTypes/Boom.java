package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

/**
 * Created by yibin on 16/4/26.
 */
public class Boom extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        Integer size = LocalTools.getSizeOfCards(cards);
        if(size != 4 && size != 2){
            return false;
        }
        if(size == 2){
            if(cards.get(CardModule.CARD_BLACK_KING) == 1
                    && cards.get(CardModule.CARD_RED_KING) == 1){
                setCardInfo(CardModule.CARD_BLACK_KING, 1, 2);
                return true;
            }
        }
        if(size == 4){
            for(CardModule card : CardModule.values()){
                if(cards.get(card) == 4){
                    setCardInfo(card, 4, 1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_BLACK_KING.getValue();
    }

    @Override
    public GroupType newInstance() {
        return new Boom();
    }
}
