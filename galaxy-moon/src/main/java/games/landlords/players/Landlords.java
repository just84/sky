package games.landlords.players;

import games.landlords.ActionInfo;
import games.landlords.Cards;
import games.landlords.Role;

/**
 * Created by yibin on 16/7/12.
 */
public class Landlords extends Player {
    public Landlords(String cards) {
        super(cards, Role.landlord);
    }

    public Landlords(Cards cards) {
        super(cards, Role.landlord);
    }

    @Override
    public ActionInfo action(){
        return super.action();
    }
}
