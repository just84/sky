package games.landlords;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import utils.SortUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yibin on 16/4/26.
 */
public class Host {
    private static boolean win = false;
    private static Role winner = null;

    public static void start() {
        Map<Double, CardModule> fullCards = Maps.newHashMap();
        List<Double> keys = Lists.newArrayList();
        for (int i = 0; i < 54; i++) {
            double random = Math.random();
            if (fullCards.containsKey(random)) {
                i--;
                continue;
            }
            fullCards.put(random, CardModule.getCardByValue(i > 51 ? i - 39 : i / 4));
            keys.add(random);
        }
        Collections.sort(keys);
        List<Player> players = Lists.newLinkedList();
        players.add(new Player(getCards(fullCards, keys, 0, 20), Role.landlord));
        players.add(new Player(getCards(fullCards, keys, 20, 17), Role.nPlayer));
        players.add(new Player(getCards(fullCards, keys, 37, 17), Role.pPlayer));

        int round = 0;
        Cards actionCards;
        System.out.println("game start!");
        System.out.println("landlord:\t" + players.get(0).showCards());
        System.out.println("nPlayer:\t" + players.get(1).showCards());
        System.out.println("pPlayer:\t" + players.get(2).showCards());
        while (!win) {
            actionCards = players.get(round % 3).action();
            players.get((round + 1) % 3).getNoticed(actionCards, Role.getRoleByIndex(round % 3));
            players.get((round + 2) % 3).getNoticed(actionCards, Role.getRoleByIndex(round % 3));
            System.out.println();
            System.out.println("role: " + Role.getRoleByIndex(round % 3).name());
            System.out.println("action: " + actionCards.toString());
            System.out.println("landlord:\t" + players.get(0).showCards());
            System.out.println("nPlayer:\t" + players.get(1).showCards());
            System.out.println("pPlayer:\t" + players.get(2).showCards());
            round++;
        }
        System.out.println();
        System.out.println(winner.name() + " win!");
        System.out.println("game over!");
        System.out.println("landlord:\t" + players.get(0).showCards());
        System.out.println("nPlayer:\t" + players.get(1).showCards());
        System.out.println("pPlayer:\t" + players.get(2).showCards());
    }

    private static Cards getCards(Map<Double, CardModule> fullCards, List<Double> keys, int start, int size) {
        Cards result = Cards.newEmptyCards();
        for (int i = 0; i < size; i++) {
            CardModule card = fullCards.get(keys.get(start + i));
            result.put(card, result.get(card) + 1);
        }
        return result;
    }

    public static void setWin(Role role) {
        win = true;
        winner = role;
    }
}
