package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 *RestTemplate配置类（XXbase项目）
 * 二次请求的配置
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 */
@Configuration
public class RestTempleatConfig {

        @Bean
        public RestTemplate restTemplate() {
            // 使用Apache HttpClient构建RestTemplate, 支持的比Spring自带的更多
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            // 去除默认的String转换器
            restTemplate.getMessageConverters().removeIf(converter -> converter instanceof StringHttpMessageConverter);
            // 添加自定义的String转换器, 支持UTF-8
            restTemplate.getMessageConverters()
                    .add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            return restTemplate;
        }
    }

