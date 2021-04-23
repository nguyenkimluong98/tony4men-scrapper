package com.lengmin.tony.crawler.main.utils;

import com.lengmin.tony.crawler.main.configuration.ApplicationConfiguration;
import lombok.experimental.UtilityClass;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import sun.security.ssl.SSLSocketFactoryImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

@UtilityClass
public class JSoupUtils {

    private static ApplicationConfiguration configuration;

    public static void init(ApplicationConfiguration configuration) {
        JSoupUtils.configuration = configuration;
    }

    public static Document get(String url) {
        try {
            return connect(url).get();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static Connection connect(String url) {

        Connection connection = Jsoup.connect(url)
                .userAgent(configuration.getUserAgent())
                .sslSocketFactory(socketFactory());

        if (configuration.getIsProxyUsed()) {
            connection.proxy(configuration.getProxyHost(), configuration.getProxyPort());
        }

        return connection;
    }

    private static SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory result = sslContext.getSocketFactory();

            return result;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
}
