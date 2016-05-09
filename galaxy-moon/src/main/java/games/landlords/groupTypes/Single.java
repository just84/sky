package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class Single extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        if(LocalTools.getSizeOfCards(cards) != 1){
            return false;
        }
        for(CardModule card : CardModule.values()){
            if(cards.get(card) == 1){
                setCardInfo(card, 1, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_RED_KING.getValue();
    }
}
