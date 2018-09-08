package shenzhen.teamway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-08-29 09:55
 **/
public class PropertiesUtils {
    static Logger log = LoggerFactory.getLogger(PropertiesUtils.class);

    static Properties p = new Properties();

    static {
        final ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
        final InputStream in = classLoader.getResourceAsStream("conf.properties");
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("读取properties配置文件异常", e);
        }
    }


    public static String getValue(String key) {
        final String property = p.getProperty(key);
        if (property == null) {
            log.error("获取到的properties值为空");
        }
        return property;
    }


    public static void main(String[] args) {
        final String port = PropertiesUtils.getValue("port");
        System.out.println(port);
    }
}