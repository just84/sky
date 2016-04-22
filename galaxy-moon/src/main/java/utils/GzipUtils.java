package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yibin on 16/4/21.
 */
public class GzipUtils {

    private static final Logger logger = LoggerFactory.getLogger(GzipUtils.class);
    public static final byte[] BYTES = new byte[0];

    private GzipUtils() {
    }

    /**
     * 压缩GZip
     */
    public static byte[] enGZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            logger.error("error", e);
        }
        return b;
    }

    /**
     * 解压GZip
     */
    public static byte[] unGZip(byte[] data) {
        if (null == data || 0 == data.length) {
            return BYTES;
        }

        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            bis.close();
        } catch (Exception e) {
            logger.error("error", e);
        }
        return b;
    }

}
