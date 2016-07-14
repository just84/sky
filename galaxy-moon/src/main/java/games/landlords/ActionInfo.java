package games.landlords;

import games.landlords.groupTypes.GroupType;

/**
 * Created by yibin on 16/7/12.
 */
public class ActionInfo {
    private Role actionRole;
    private Cards actionCards;
    private GroupType groupType;

    private ActionInfo(){}

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

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public static ActionInfo newOne(Role role, Cards cards){
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setActionRole(role);
        actionInfo.setActionCards(cards);
        actionInfo.setGroupType(LocalTools.getGroupType(cards));
        return actionInfo;
    }

    @Override
    public String toString() {
        return "ActionInfo{" +
                "actionRole=" + actionRole +
                ", actionCards=" + actionCards +
                ", groupType=" + groupType +
                '}';
    }
}
