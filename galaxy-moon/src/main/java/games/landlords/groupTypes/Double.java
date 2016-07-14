package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class Double extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        if(LocalTools.getSizeOfCards(cards) != 2){
            return false;
        }
        for(CardModule card : CardModule.values()){
            if(cards.get(card) == 2){
                setCardInfo(card, 2, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_2.getValue();
    }

    @Override
    public GroupType newInstance() {
        return new Double();
    }
}
