import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by yibin on 16/3/24.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(DateUtils.parseDate("201512131434", "yyyyMMddHHmm"));
    }
}