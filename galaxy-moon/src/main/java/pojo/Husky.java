package pojo;

/**
 * Created by yibin on 16/4/22.
 */
public class Husky implements Dog,Action {
    private static Husky husky;
    Action action = new Action() {
        public String move() {
            return "jump";
        }

        @Override
        public String talk() {
            return "ao ao ao";
        }
    };

    public Husky(){}

    public final String color() {
        System.out.println("color");
        return "black";
    }

    public String type() {
        System.out.println("type");
        return "husky";
    }

    public final String move() {
        System.out.println("move");
        return action.move();
    }

    public String talk() {
        System.out.println("talk");
        return action.talk();
    }

//    public static Husky getInstance(){
//        if (husky == null) {
//            husky = new Husky();
//        }
//        return husky;
//    }
}
