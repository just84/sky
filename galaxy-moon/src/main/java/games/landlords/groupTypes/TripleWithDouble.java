package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class TripleWithDouble extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        if(LocalTools.getSizeOfCards(cards) != 5){
            return false;
        }
        CardModule mainCard = null;
        List<CardModule> attachCards = Lists.newArrayList();
        for(CardModule card : CardModule.values()){
            int cardSize = cards.get(card);
            if(cardSize != 3 && cardSize != 2 && cardSize != 0){
                return false;
            }
            if(cardSize == 3){
                mainCard = card;
            }
            if(cardSize == 2){
                attachCards.add(card);
            }
        }
        if(mainCard != null && attachCards.size() == 1){
            setCardInfo(mainCard, 3, 1, attachCards, 2, 1);
            return true;
        }
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_2.getValue();
    }

    @Override
    public GroupType newInstance() {
        return new TripleWithDouble();
    }
}
