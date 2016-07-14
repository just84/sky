package games.landlords.players;

import games.landlords.ActionInfo;
import games.landlords.Cards;
import games.landlords.Role;

/**
 * Created by yibin on 16/7/12.
 */
public class PPlayer extends Player {
    public PPlayer(String cards) {
        super(cards, Role.pPlayer);
    }

    public PPlayer(Cards cards) {
        super(cards, Role.pPlayer);
    }

    @Override
    public ActionInfo action(){
        return super.action();
    }
}
