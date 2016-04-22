package use.forecast.stock;

/**
 * Created by yibin on 16/3/31.
 */
public class Judge {
    private Integer range;
    private Double weight;
    private Double like;
    private Double timeWeight;

    public Judge() {
        like = 0.8d;
        weight = 1d;
        timeWeight = 2d;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLike() {
        return like;
    }

    public void setLike(Double like) {
        this.like = like;
    }

    public Double getTimeWeight() {
        return timeWeight;
    }

    public void setTimeWeight(Double timeWeight) {
        this.timeWeight = timeWeight;
    }
}
