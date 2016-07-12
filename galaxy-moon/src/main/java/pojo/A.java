package pojo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by yibin on 16/5/30.
 */
public class A {

    @NotBlank
    private String i;
    @NotEmpty
    private String j;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    private volatile static A a = null;
    public static A getInstance(){
        if(a == null){
            synchronized (A.class){
                if(a == null){
                    a = new A();
                }
            }
        }
        return a;
    }
}
