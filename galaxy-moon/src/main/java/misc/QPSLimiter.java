package misc;

import api.Processor;

/**
 * Created by yibin on 16/4/7.
 */
public class QPSLimiter {
    private int idle;
    private int max;
    private long lastTime;

    public QPSLimiter(int size) {
        this.max = size;
        this.idle = size;
        lastTime = System.currentTimeMillis();
    }

    public boolean hasIdle() {
        freshIdle();
        return idle > 0;
    }

    public <Param, Result> Result deal(Param param, Processor<Param, Result> processor) {
        if (!hasIdle()) {
            return null;
        }
        idle--;
        return processor.process(param);
    }

    private void freshIdle(){
        long current = System.currentTimeMillis();
        //每经过unit毫秒，idle+1
        int unit = 1000 / max;
        long addition = Math.floorDiv((current - lastTime), unit);
        idle = (int)Math.min(max, idle + addition);
        lastTime += addition * unit;
    }
}
