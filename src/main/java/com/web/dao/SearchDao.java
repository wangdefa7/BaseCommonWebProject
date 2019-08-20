package com.web.dao;

import com.web.core.CommonResult;
import com.web.model.CommonEntity;
import com.web.secondRequest.HttpUtil;
import com.web.util.EsQueryDao;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.web.constant.Constant.*;

/**
 *查询数据Dao类（XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
@Repository
public class SearchDao{

    Logger log = LoggerFactory.getLogger(SearchDao.class);

    @Autowired
    TransportClient client;

    @Autowired
    EsQueryDao esQueryDao;

    String Keyword = ".keyword";//分词

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//时间规则

    /**
     * 二次请求数据
     * 传入map,对map进行处理
     * @param map, commonEntity
     * @return List
     */
    public List searchTwice(Map map, CommonEntity commonEntity){
        HttpUtil httpUtil = new HttpUtil();
        if (map.get("dt")!=null){
            try {
                log.info("查询时间："+map.get("dt"));
                map.put("startDate",sdf.format(sdf.parse(commonEntity.getStartDate())));
                map.put("endDate", sdf.format(sdf.parse(commonEntity.getEndDate())));
            } catch (ParseException e) {
                System.out.println("时间格式错误");
                e.printStackTrace();
            }
        }
        if (map.get("pageSize")!=null){
            map.put("pageSize",map.get("pageSize"));
        }else {
            map.put("pageSize",10);
        }
        return httpUtil.HttpCommonPost(IP_Twice_Search,map);
    }


    /**
     * 查询es数据
     * @param  map, commonEntity
     * @return CommonResult
     */
    public CommonResult searchEs(Map map){
        CommonEntity commonEntity = new CommonEntity();
        CommonResult commonResult = new CommonResult();
        SearchHits searchHits;
        List list = new ArrayList();
        commonEntity.setStartDate(map.get("startDate"));
        commonEntity.setEndDate(map.get("endDate"));
        commonEntity.setPageNum(map.get("pageNum"));
        commonEntity.setPageSize(map.get("pageSize"));
        commonEntity.setValue(map.get("value"));
        if (!map.get("").equals("")){
            searchHits = searchMethod(map,commonEntity);
        }else {
            searchHits = searchMethod(map,commonEntity);
        }
        searchHits.forEach(i->{
            Map<String,Object> map1=i.getSourceAsMap();
            list.add(map1);
        });
        log.info("查询数量"+list.size());
        commonResult.total= (int) searchHits.getTotalHits();
        commonResult.list = list;
        return commonResult;
    }

    /**
     *
     * @param map
     * @return
     */
    public SearchHits searchMethod(Map map,CommonEntity commonEntity){
        SearchResponse searchResponse;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!map.get("查询值").equals("")){
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("字段名称"+Keyword,"*"+ QueryParser.escape(String.valueOf(map.get("查询值"))) +"*"));
        }
        if (!commonEntity.getStartDate().equals("")||!commonEntity.getEndDate().equals("")){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("时间")
                    .from(commonEntity.getStartDate()).to(commonEntity.getEndDate()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(commonEntity.getPageSize());
        searchSourceBuilder.from(commonEntity.getPageNum());
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        if(map.get("排序字段").equals("true")){
            searchSourceBuilder.sort(new FieldSortBuilder("排序的字段").order(SortOrder.ASC));
        }else if (map.get("排序字段").equals("false")){
            searchSourceBuilder.sort(new FieldSortBuilder("排序的字段").order(SortOrder.DESC));
        }
        searchResponse = client.prepareSearch(Index_ES)
                .setTypes(Type_ES)
                .setSource(searchSourceBuilder)
                .get();
        return searchResponse.getHits();
    }
}
