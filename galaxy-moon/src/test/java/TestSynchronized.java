import pojo.A;

/**
 * Created by yibin on 2016/9/29.
 */
public class TestSynchronized {
    public void print1(){
        synchronized (""){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1"+System.currentTimeMillis());
        }
    }

    public synchronized static void print2() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("2" + System.currentTimeMillis());
    }

    public static void print3(){
        synchronized (TestSynchronized.class) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("3" + System.currentTimeMillis());
        }
    }

    public void print4() {
        synchronized (this) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("4" + System.currentTimeMillis());
        }
    }
}
