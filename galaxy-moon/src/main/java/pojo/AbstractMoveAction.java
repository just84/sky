package pojo;

/**
 * Created by yibin on 16/4/22.
 */
public abstract class AbstractMoveAction implements Action {
    @Override
    public String move(){
        return "run";
    }
}
