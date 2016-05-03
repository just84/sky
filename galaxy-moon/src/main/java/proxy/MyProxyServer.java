package proxy;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.spi.DefaultProxySelector;
import utils.GzipUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by yibin on 16/4/20.
 * target:
 * 1.直传
 * 2.分析数据
 * 3.修改数据
 * 4.行为分析
 * final.幻
 */
public class MyProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(MyProxyServer.class);

    public void start(int port){
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            Proxy proxy = new Proxy(Proxy.Type.DIRECT, serverSocket.getLocalSocketAddress());
            ProxySelector proxySelector = new DefaultProxySelector();

            while (true){
                logger.info("start to wait");
                Socket socket = serverSocket.accept();
                logger.info("get one, socket:{}", socket);
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                int length = 0;
                while((line = reader.readLine()) != null){
                    System.out.println(line);
                    if(line.equals("\r\n") || line.isEmpty()){
                        break;
                    }
                    if(line.contains("Content-Length: ")){
                        length = Integer.valueOf(line.substring(16));
                    }
                }

                System.out.println("yibin debug, length = "+length);
                char[] data = new char[length];
                reader.read(data);
                byte[] unZipData = GzipUtils.unGZip(new String(data).getBytes());

                String stringData;
                if(unZipData == null){
                    stringData = new String(data);
                } else {
                    stringData = new String(unZipData);
                }
                System.out.println("yibin debug, stringData = "+stringData);

                reader.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
