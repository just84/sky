package games.landlords;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import games.landlords.players.Landlords;
import games.landlords.players.NPlayer;
import games.landlords.players.PPlayer;
import games.landlords.players.Player;

import java.util.*;

/**
 * Created by yibin on 16/4/26.
 */
public class Host {
    private static boolean win = false;
    private static Role winner = null;
    private static LinkedList<ActionInfo> record = Lists.newLinkedList();

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
        players.add(new Landlords(getCards(fullCards, keys, 0, 20)));
        players.add(new NPlayer(getCards(fullCards, keys, 20, 17)));
        players.add(new PPlayer(getCards(fullCards, keys, 37, 17)));

        int round = 0;
        System.out.println("[HOST] game start!");
        System.out.println("landlord:\n" + players.get(0).showAllInfo());
        System.out.println("nPlayer:\n" + players.get(1).showAllInfo());
        System.out.println("pPlayer:\n" + players.get(2).showAllInfo());
        while (!win) {
            Player player = players.get(round % 3);
            ActionInfo actionInfo = player.action();
            players.get((round + 1) % 3).getNoticed(actionInfo);
            players.get((round + 2) % 3).getNoticed(actionInfo);
            record.add(actionInfo);
            System.out.println();
            System.out.println("[HOST] role: " + actionInfo.getActionRole().name());
            System.out.println("[HOST] action: " + actionInfo.getActionCards().toString());
            System.out.println("landlord:\n" + players.get(0).showAllInfo());
            System.out.println("nPlayer:\n" + players.get(1).showAllInfo());
            System.out.println("pPlayer:\n" + players.get(2).showAllInfo());
            round++;
        }
        System.out.println();
        System.out.println("[HOST] " + winner.name() + " win!");
        System.out.println("[HOST] game over!");
        System.out.println("landlord:\n" + players.get(0).showAllInfo());
        System.out.println("nPlayer:\n" + players.get(1).showAllInfo());
        System.out.println("pPlayer:\n" + players.get(2).showAllInfo());
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
