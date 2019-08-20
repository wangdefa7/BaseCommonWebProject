package com.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *连接ES配置信息（XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
@ConfigurationProperties(prefix = "elasticsearch.config")
@Component
public class EsProperties {

    private List<String> host;
    private int port;
    private String cluster;

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

}
