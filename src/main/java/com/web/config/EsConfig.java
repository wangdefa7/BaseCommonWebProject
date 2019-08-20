package com.web.config;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 *连接ES配置类（XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsConfig {

    Logger logger = LoggerFactory.getLogger(EsConfig.class);

    @Autowired
    EsProperties esProperties;

    @Bean
    public TransportClient config(){
        Settings settings = Settings.builder()
                .put("cluster.name",esProperties.getCluster())
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        List<String> list = esProperties.getHost();
            try {
                for (String host : list) {
                    client = client.addTransportAddress(new TransportAddress(InetAddress.getByName(host),
                            esProperties.getPort()));
                }
            } catch (UnknownHostException e) {
                logger.warn("配置信息异常，ip地址报错");
                e.printStackTrace();
            }
        return client;
    }


}
