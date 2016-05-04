package games.landlords.groupTypes;

import com.google.common.collect.Lists;
import games.landlords.CardModule;
import games.landlords.Cards;
import games.landlords.LocalTools;
import utils.Utils;

import java.util.Collection;
import java.util.List;

/**
 * Created by yibin on 16/4/27.
 */
public abstract class AbstractGroupType implements GroupType {

    /**
     * 主卡，出牌主体的最小卡，如44455568：4
     */
    private CardModule mainCard;
    /**
     * 主卡大小，如33344468：3
     */
    private Integer mainCardSize;
    /**
     * 主卡长度，如33344468：2
     */
    private Integer mainCardLength;
    /**
     * 附卡，如33344468：[6,8]
     */
    private List<CardModule> attachCards;
    /**
     * 附卡大小，如33344468：1
     */
    private Integer attachCardSize;
    /**
     * 附卡长度，如33344468：2
     */
    private Integer attachCardLength;

    @Override
    public List<Cards> getSolution(Cards selfCards, Cards cards) {
        if (cards == null || selfCards == null || LocalTools.getSizeOfCards(selfCards) < LocalTools.getSizeOfCards(cards)) {
            return null;
        }
        if (!analyse(cards)) {
            return null;
        }

        List<Cards> mainSolutions = getMainSolutions(selfCards);

        if (mainSolutions.isEmpty()) {
            return null;
        }

        if (attachCards != null && !attachCards.isEmpty()) {
            return mainSolutions;
        }

        List<Cards> solutions = Lists.newArrayList();
        for (Cards mainSolution : mainSolutions) {
            Cards restCards = LocalTools.subCards(selfCards.copy(), mainSolution);
            List<Cards> attachSolutions = getAttachSolutions(restCards);
            if (attachSolutions == null || attachSolutions.isEmpty()) {
                continue;
            }
            for (Cards attachSolution : attachSolutions) {
                solutions.add(LocalTools.addCards(attachSolution, mainSolution));
            }
        }
        return solutions;
    }

    private List<Cards> getMainSolutions(Cards selfCards) {
        if(mainCard == null || mainCardSize == 0 || mainCardLength == 0){
            throw new IllegalArgumentException(
                    String.format("main card data is illegal,mainCard:%s, mainCardSize:%d, mainCardLength:%d",
                            mainCard, mainCardSize, mainCardLength));
        }
        List<Cards> mainSolutions = Lists.newArrayList();
        for (Integer index = mainCard.getValue() + 1; index <= getTop() - mainCardLength; index++) {
            CardModule card = CardModule.getCardByValue(index);
            boolean usable = true;
            for (int i = index; i < index + mainCardLength; i++) {
                if (selfCards.get(card) < mainCardSize) {
                    usable = false;
                    index = i;
                    break;
                }
            }
            if (usable) {
                Cards solution = Cards.newEmptyCards();
                for (int i = index; i < index + mainCardLength; i++) {
                    solution.put(CardModule.getCardByValue(i), mainCardSize);
                }
                mainSolutions.add(solution);
            }
        }
        return mainSolutions;
    }

    private List<Cards> getAttachSolutions(Cards restCards) {
        if(attachCards == null || attachCards.size() == 0 || attachCardSize == 0 || attachCardLength == 0){
            return Lists.newArrayList();
        }
        List<CardModule> usableCards = Lists.newArrayList();
        for (CardModule cardModule : CardModule.values()) {
            if (restCards.get(cardModule) >= attachCardSize) {
                usableCards.add(cardModule);
            }
        }
        List<Cards> attachSolutions = Lists.newArrayList();
        Collection<Collection<CardModule>> selections = Utils.selectMfromN(usableCards, attachCardLength);
        for (Collection<CardModule> selection : selections) {
            Cards card = Cards.newEmptyCards();
            for (CardModule cardModule : selection) {
                card.put(cardModule, attachCardSize);
            }
            attachSolutions.add(card);
        }

        return attachSolutions;
    }

    public CardModule getMainCard() {
        return mainCard;
    }

    public void setMainCard(CardModule mainCard) {
        this.mainCard = mainCard;
    }

    public Integer getMainCardSize() {
        return mainCardSize;
    }

    public void setMainCardSize(Integer mainCardSize) {
        this.mainCardSize = mainCardSize;
    }

    public Integer getMainCardLength() {
        return mainCardLength;
    }

    public void setMainCardLength(Integer mainCardLength) {
        this.mainCardLength = mainCardLength;
    }

    public List<CardModule> getAttachCards() {
        return attachCards;
    }

    public void setAttachCards(List<CardModule> attachCards) {
        this.attachCards = attachCards;
    }

    public Integer getAttachCardSize() {
        return attachCardSize;
    }

    public void setAttachCardSize(Integer attachCardSize) {
        this.attachCardSize = attachCardSize;
    }

    public Integer getAttachCardLength() {
        return attachCardLength;
    }

    public void setAttachCardLength(Integer attachCardLength) {
        this.attachCardLength = attachCardLength;
    }

    public void setCardInfo(CardModule mainCard, Integer mainCardSize, Integer mainCardLength){
        setCardInfo(mainCard, mainCardSize, mainCardLength, null, 0, 0);
    }

    public void setCardInfo(CardModule mainCard, Integer mainCardSize, Integer mainCardLength,
                            List<CardModule> attachCards, Integer attachCardSize, Integer attachCardLength ){
        this.mainCard = mainCard;
        this.mainCardSize = mainCardSize;
        this.mainCardLength = mainCardLength;
        this.attachCards = attachCards;
        this.attachCardSize = attachCardSize;
        this.attachCardLength = attachCardLength;
    }
}
