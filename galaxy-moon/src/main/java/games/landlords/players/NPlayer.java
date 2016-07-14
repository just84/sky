package games.landlords.players;

import games.landlords.ActionInfo;
import games.landlords.Cards;
import games.landlords.Role;

/**
 * Created by yibin on 16/7/12.
 */
public class NPlayer extends Player {
    public NPlayer(String cards) {
        super(cards, Role.nPlayer);
    }

    public NPlayer(Cards cards) {
        super(cards, Role.nPlayer);
    }

    @Override
    public ActionInfo action(){
        return super.action();
    }
}
