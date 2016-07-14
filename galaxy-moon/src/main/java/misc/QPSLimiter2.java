package misc;

import api.Processor;
import com.google.common.collect.Queues;

import java.util.Queue;

/**
 * Created by yibin on 16/7/14.
 * 用队列记录访问时间进行限流，有一定的内存占用，能保证每一秒内的请求量都在限制范围内
 */
public class QPSLimiter2 {
    private Queue<Long> recordQueue;

    public QPSLimiter2(int size){
        recordQueue = Queues.newConcurrentLinkedQueue();
        for(int i = 0;i<size;i++){
            recordQueue.add(0l);
        }
    }

    public boolean hasIdle() {
        return System.currentTimeMillis() - recordQueue.peek() > 1000;
    }

    public <Param, Result> Result deal(Param param, Processor<Param, Result> processor) {
        if (!hasIdle()) {
            return null;
        }
        recordNewOne(System.currentTimeMillis());
        return processor.process(param);
    }

    private void recordNewOne(long timeMillis) {
        recordQueue.remove();
        recordQueue.add(timeMillis);
    }
}
