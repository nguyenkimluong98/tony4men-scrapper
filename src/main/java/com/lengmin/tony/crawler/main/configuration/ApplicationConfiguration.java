package com.lengmin.tony.crawler.main.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */


@Data
@Configuration
public class ApplicationConfiguration {

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("#{new Integer('${proxy.port}')}")
    private Integer proxyPort;

    @Value("#{new Boolean('${proxy.used}')}")
    private Boolean isProxyUsed;

    @Value("${agent}")
    private String userAgent;

    @Value("${website}")
    private String website;
}
