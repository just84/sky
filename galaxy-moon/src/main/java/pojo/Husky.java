package pojo;

/**
 * Created by yibin on 16/4/22.
 */
public class Husky implements Dog,Action {
    private static Husky husky;
    AbstractTalkAction abstractTalkAction = new AbstractTalkAction() {
        public String move() {
            return null;
        }
    };
    AbstractMoveAction abstractMoveAction = new AbstractMoveAction() {
        public String talk() {
            return null;
        }
    };

    private Husky(){}

    public String color() {
        return "black";
    }

    public String type() {
        return "husky";
    }

    public String move() {
        return abstractMoveAction.move();
    }

    public String talk() {
        return abstractTalkAction.talk();
    }

    public static Husky getInstance(){
        if (husky == null) {
            husky = new Husky();
        }
        return husky;
    }
}
