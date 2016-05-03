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
        return false;
    }

    @Override
    public int getTop() {
        return 0;
    }
}
