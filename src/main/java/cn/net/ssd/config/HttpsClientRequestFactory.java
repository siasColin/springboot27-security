package cn.net.ssd.config;

import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.HttpURLConnection;
import java.security.KeyStore;

/**
 * 继承SimpleClientHttpRequestFactory 根据连接支持实现http和https请求发送
 */
public class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {
    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
        try {
            if (!(connection instanceof HttpsURLConnection)) {
                //http协议
                super.prepareConnection(connection, httpMethod);
            }
            if ((connection instanceof HttpsURLConnection)) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
                //https协议
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                //信任任何链接(也就是忽略认证)
                TrustStrategy anyTrustStrategy = (chain, authType) -> true;
                SSLContext ctx = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                httpsURLConnection.setSSLSocketFactory(ctx.getSocketFactory());
                super.prepareConnection(httpsURLConnection, httpMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}