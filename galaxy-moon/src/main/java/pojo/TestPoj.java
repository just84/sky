package pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yibin on 16/5/16.
 */
public class TestPoj {
    private static boolean flag = false;

    private static final Logger logger = LoggerFactory.getLogger(TestPoj.class);

    public static String startWait(){
        while(!flag){
            try {
                logger.info("wait flag:{}",flag);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "finish";
    }

    public static void wakeup(){
        flag = true;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
