package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;

import java.util.List;

/**
 * Created by yibin on 16/4/27.
 */
public class QuadraSentenceWithSingle extends AbstractGroupType {
    @Override
    public boolean analyse(Cards cards) {
        return false;
    }

    @Override
    public int getTop() {
        return 0;
    }
}