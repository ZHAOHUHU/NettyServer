package shenzhen.teamway.ssl;

import org.springframework.core.io.ClassPathResource;
import shenzhen.teamway.utils.PropertiesUtils;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-08-27 09:17
 **/
public class ContextSSLFactory {

    private static SSLContext SSL_CONTEXT_S = null;

    private static SSLContext SSL_CONTEXT_C = null;
    private static  String keyStorePass ;
    static {
        keyStorePass=  PropertiesUtils.getValue("keyStorePass");
        SSLContext sslContext = null;
        SSLContext sslContext2 = null;
        try {
            sslContext = SSLContext.getInstance("SSLv3");
            sslContext2 = SSLContext.getInstance("SSLv3");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            if (getKeyManagersServer() != null && getTrustManagersServer() != null) {
                sslContext.init(getKeyManagersServer(), getTrustManagersServer(), null);
            }
            if (getKeyManagersClient() != null && getTrustManagersClient() != null) {
                sslContext2.init(getKeyManagersClient(), getTrustManagersClient(), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        sslContext.createSSLEngine().getSupportedCipherSuites();
        sslContext2.createSSLEngine().getSupportedCipherSuites();
        SSL_CONTEXT_S = sslContext;
        SSL_CONTEXT_C = sslContext2;
    }

    public ContextSSLFactory() {

    }

    public static SSLContext getSslContext() {
        return SSL_CONTEXT_S;
    }

    public static SSLContext getSslContext2() {
        return SSL_CONTEXT_C;
    }

    private static TrustManager[] getTrustManagersServer() {
        FileInputStream is = null;
        KeyStore ks = null;
        TrustManagerFactory keyFac = null;

        TrustManager[] kms = null;
        try {
            // 获得KeyManagerFactory对象. 初始化位默认算法   签名算法名称: SHA256withRSA
            keyFac = TrustManagerFactory.getInstance("SunX509");
            is = new FileInputStream((new ClassPathResource("conf/sChat.jks")).getFile());
            ks = KeyStore.getInstance("JKS");
            //keystore密码
            ks.load(is, keyStorePass.toCharArray());
            keyFac.init(ks);
            kms = keyFac.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kms;
    }

    private static TrustManager[] getTrustManagersClient() {
        FileInputStream is = null;
        KeyStore ks = null;
        TrustManagerFactory keyFac = null;

        TrustManager[] kms = null;
        try {
            // 获得KeyManagerFactory对象. 初始化位默认算法
            keyFac = TrustManagerFactory.getInstance("SunX509");
            is = new FileInputStream((new ClassPathResource("conf/cChat.jks")).getFile());
            ks = KeyStore.getInstance("JKS");
            ks.load(is, keyStorePass.toCharArray());
            keyFac.init(ks);
            kms = keyFac.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kms;
    }

    private static KeyManager[] getKeyManagersServer() {
        FileInputStream is = null;
        KeyStore ks = null;
        KeyManagerFactory keyFac = null;

        KeyManager[] kms = null;
        try {
            // 获得KeyManagerFactory对象. 初始化为默认算法
            keyFac = KeyManagerFactory.getInstance("SunX509");
            is = new FileInputStream((new ClassPathResource("/conf/sChat.jks")).getFile());
            ks = KeyStore.getInstance("JKS");
            ks.load(is, keyStorePass.toCharArray());
            keyFac.init(ks, keyStorePass.toCharArray());
            kms = keyFac.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kms;
    }

    private static KeyManager[] getKeyManagersClient() {
        FileInputStream is = null;
        KeyStore ks;
        KeyManagerFactory keyFac;

        KeyManager[] kms = null;
        try {
            // 获得KeyManagerFactory对象. 初始化位默认算法
            keyFac = KeyManagerFactory.getInstance("SunX509");
            is = new FileInputStream((new ClassPathResource("conf/cChat.jks")).getFile());
            ks = KeyStore.getInstance("JKS");
            ks.load(is, keyStorePass.toCharArray());
            keyFac.init(ks, keyStorePass.toCharArray());
            kms = keyFac.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kms;
    }
}