package pojo;

/**
 * Created by yibin on 2018/4/20.
 */
public class Poj implements Dog{
    private String name;

    public String getName() {
        System.out.println("Poj.getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("Poj.setName");
        this.name = name;
    }

    @Override
    public String color() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }
}
