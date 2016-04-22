package pojo;

/**
 * Created by yibin on 16/4/22.
 */
public class Hasaki implements Dog,Action {
    private static Hasaki hasaki;
    AbstractTalkAction abstractTalkAction = new AbstractTalkAction() {
        @Override
        public String move() {
            return null;
        }
    };
    AbstractMoveAction abstractMoveAction = new AbstractMoveAction() {
        @Override
        public String talk() {
            return null;
        }
    };

    private Hasaki(){}

    @Override
    public String color() {
        return "black";
    }

    @Override
    public String type() {
        return "hasaki";
    }

    @Override
    public String move() {
        return abstractMoveAction.move();
    }

    @Override
    public String talk() {
        return abstractTalkAction.talk();
    }

    public static Hasaki getInstance(){
        if (hasaki == null) {
            hasaki = new Hasaki();
        }
        return hasaki;
    }
}
