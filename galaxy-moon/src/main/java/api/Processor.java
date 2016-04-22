package api;

/**
 * Created by yibin on 16/4/7.
 */
public interface Processor<Param, Result> {
    public Result process(Param param);
}
