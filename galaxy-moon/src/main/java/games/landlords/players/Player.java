package games.landlords.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import games.landlords.*;
import games.landlords.ActionInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 16/4/25.
 */
public abstract class Player {
    private Map<Role, Cards> record;
    private Cards knownCardsRecord;
    private Cards unknownCardsRecord;
    private Cards selfCards;
    private ActionInfo lastAction;
    private Role role;

    public Player(String cards, Role role) {
        init();
        initCards(cards);
        this.role = role;
    }

    public Player(Cards cards, Role role) {
        init();
        initCards(cards);
        this.role = role;
    }

    private void init() {
        record = Maps.newHashMap();
        record.put(Role.landlord, Cards.newEmptyCards());
        record.put(Role.nPlayer, Cards.newEmptyCards());
        record.put(Role.pPlayer, Cards.newEmptyCards());
        selfCards = Cards.newEmptyCards();
        knownCardsRecord = Cards.newEmptyCards();
        unknownCardsRecord = Cards.newFullCards();
        lastAction = ActionInfo.newOne(Role.landlord, Cards.newEmptyCards());
    }

    private void initCards(String cards) {
        initCards(LocalTools.splitCards(cards));
    }

    private void initCards(Cards cards) {
        LocalTools.addCards(selfCards, cards);
        LocalTools.addCards(knownCardsRecord, cards);
        LocalTools.subCards(unknownCardsRecord, cards);
    }

    public void getNoticed(ActionInfo actionInfo) {
        Cards actionCards = actionInfo.getActionCards();
        Role actionRole = actionInfo.getActionRole();
        if (actionCards != null && LocalTools.getSizeOfCards(actionCards) != 0) {
            LocalTools.addCards(record.get(actionRole), actionCards);
            if(actionRole.getIndex() != role.getIndex()) {
                LocalTools.addCards(knownCardsRecord, actionCards);
                LocalTools.subCards(unknownCardsRecord, actionCards);
            }
            lastAction = actionInfo;
        }
    }

    public ActionInfo action() {
        Cards actionResult = Cards.newEmptyCards();
        if (lastAction.getActionRole().equals(role)) {//自出
            for (CardModule cardModule : CardModule.values()) {
                if (selfCards.get(cardModule) > 0) {
                    actionResult.put(cardModule, selfCards.get(cardModule));
                    break;
                }
            }
        } else if (role.equals(Role.landlord)) {//地主决策
            List<Cards> solutions = getSolutions(lastAction);
            if (solutions == null || solutions.size() == 0) {
                return ActionInfo.newOne(role,Cards.newEmptyCards());
            }
            actionResult = solutions.get(0);
        } else {//农民决策
            if (lastAction.getActionRole().equals(Role.landlord)) {
                List<Cards> solutions = getSolutions(lastAction);
                if (solutions == null || solutions.size() == 0) {
                    return ActionInfo.newOne(role, Cards.newEmptyCards());
                }
                actionResult = solutions.get(0);
            }
        }
        LocalTools.subCards(selfCards, actionResult);
        ActionInfo action = ActionInfo.newOne(role, actionResult);
        getNoticed(action);
        if (LocalTools.getSizeOfCards(selfCards) == 0) {
            Host.setWin(role);
        }
        return action;
    }

    private List<Cards> getSolutions(ActionInfo action) {
        return action.getGroupType().getSolution(selfCards,action.getActionCards());
//        for (CardModule cardModule : action.keySet()) {
//            if (action.get(cardModule) == 0) {
//                continue;
//            }
//            for (Integer i = cardModule.getValue() + 1; i <= CardModule.MAX_VALUE; i++) {
//                if (selfCards.get(CardModule.getCardByValue(i)).equals(action.get(cardModule))) {
//                    List<Cards> result = Lists.newArrayList();
//                    Cards map = Cards.newEmptyCards();
//                    map.put(CardModule.getCardByValue(i), action.get(cardModule));
//                    result.add(map);
//                    return result;
//                }
//            }
//        }
//        return null;
    }

    public String showCards() {
        return selfCards.toString();
    }

    public String showAllInfo() {
        return "Player{" +
                "\n\trecord=" + record +
                "\n\tknownCardsRecord=" + knownCardsRecord +
                "\n\tunknownCardsRecord=" + unknownCardsRecord +
                "\n\tselfCards=" + selfCards +
                "\n\tlastAction=" + lastAction +
                "\n\trole=" + role +
                "\n}";
    }

    public Role getRole() {
        return role;
    }
}
