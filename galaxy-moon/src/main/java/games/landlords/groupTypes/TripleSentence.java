package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public class TripleSentence extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        return false;
    }

    @Override
    public int getTop() {
        return CardModule.CARD_K.getValue();
    }
}
