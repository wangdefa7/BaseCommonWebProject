package com.web.secondRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.web.config.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *Http请求工具类（XXbase项目）
 * 二次请求的工具类
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
public class HttpUtil {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpClient httpClient;

    /**
     * post请求的通用方法（返回List）
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public List HttpCommonPost(String url, Map<String,Object> map)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpMethod method = HttpMethod.POST;
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
        map.forEach((k,v)->{
            params.add(k,v);
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        String str= restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        System.out.println(str);
        List list= JSON.parseObject(String.valueOf(str),new TypeReference<List>(){});
        return list;
    }

    /**
     * post查询int类型的返回值
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public int HttpCommonPostTotal(String url, Map<String,Object> map)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpMethod method = HttpMethod.POST;
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
        map.forEach((k,v)->{
            params.add(k,v);
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        String str= restTemplate.exchange(url, method, requestEntity, String.class).getBody();
        System.out.println("获取Hbase查询的数量："+str);
        int i = Integer.parseInt(str);
        return i;
    }
}
