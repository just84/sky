package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class QuadraWithSingle extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        if(LocalTools.getSizeOfCards(cards) != 6){
            return false;
        }
        CardModule mainCard = null;
        List<CardModule> attachCards = Lists.newArrayList();
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 4 && cardSize != 1 && cardSize != 0){
                return false;
            }
            if(cardSize == 4){
                mainCard = card;
            }
            if(cardSize == 1){
                attachCards.add(card);
            }
        }
        if(mainCard != null && attachCards.size() == 2){
            setCardInfo(mainCard, 4, 1, attachCards, 1, 2);
            return true;
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_2.getValue();
    }
}
