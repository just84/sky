package use.forecast.stock;

import java.util.List;

/**
 * Created by yibin on 2017/6/5.
 */
public class Data {
    private String code;
    private String message;
    private List<String> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
