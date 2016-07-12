package games.landlords.groupTypes;

import games.landlords.Cards;
import games.landlords.Player;
import games.landlords.Role;

/**
 * Created by yibin on 16/7/12.
 */
public class ActionInfo {
    private Role actionRole;
    private Cards actionCards;

    public Role getActionRole() {
        return actionRole;
    }

    public void setActionRole(Role actionRole) {
        this.actionRole = actionRole;
    }

    public Cards getActionCards() {
        return actionCards;
    }

    public void setActionCards(Cards actionCards) {
        this.actionCards = actionCards;
    }

    public static ActionInfo newOne(Role role, Cards cards){
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setActionRole(role);
        actionInfo.setActionCards(cards);
        return actionInfo;
    }
}
