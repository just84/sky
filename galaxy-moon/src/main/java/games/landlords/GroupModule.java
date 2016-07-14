package games.landlords;

import games.landlords.groupTypes.*;
import games.landlords.groupTypes.Double;

import java.util.List;

/**
 * Created by yibin on 16/4/26.
 * 对出牌类型的枚举
 */
public enum GroupModule {
    Single(new Single()),
    Double(new Double()),
    Triple(new Triple()),
    Quad(new Quad()),
    Boom(new Boom()),
    TripleWithSingle(new TripleWithSingle()),
    TripleWithDouble(new TripleWithDouble()),
    QuadWithSingle(new QuadWithSingle()),
    QuadWithDouble(new QuadWithDouble()),
    Sentence(new Sentence()),
    DoubleSentence(new DoubleSentence()),
    TripleSentence(new TripleSentence()),
    TripleSentenceWithSingle(new TripleSentenceWithSingle()),
    TripleSentenceWithDouble(new TripleSentenceWithDouble()),
    QuadSentenceWithSingle(new QuadSentenceWithSingle()),
    QuadSentenceWithDouble(new QuadSentenceWithDouble());

    private GroupType groupType;

    GroupModule(GroupType groupType){
        this.groupType = groupType;
    }

    public boolean analyse(Cards cards) {
        return groupType.analyse(cards);
    }

    public GroupType getGroupType() {
        GroupType result = groupType;
        groupType = groupType.newInstance();
        return result;
    }

    public List<Cards> getSolution(Cards selfCards, Cards cards) {
        return groupType.getSolution(selfCards, cards);
    }
}
