package api;

/**
 * Created by yibin on 16/4/26.
 */
public interface ProcessorPlus<Param1, Param2, Result> {
    public Result process(Param1 param1, Param2 param2);
}

