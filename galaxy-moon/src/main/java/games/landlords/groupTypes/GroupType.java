package games.landlords.groupTypes;

import games.landlords.CardModule;
import games.landlords.Cards;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 */
public interface GroupType {

    /**
     *
     * @param selfCards
     * @param cards
     * @return
     */
    public List<Cards> getSolution(Cards selfCards, Cards cards);

    /**
     * 解析得到主卡和副卡的相关数据，由各子类实现
     * 返回成功或失败
     * @param cards
     * @return
     */
    boolean analyse(Cards cards);

    /**
     * 取得能达到的最高维的位置
     * 如，Single: 14, Double: 12, Sentence: 11
     * @return
     */
    int getTop();
}
