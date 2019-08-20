package com.web.util;


import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 *ES查询工具类（XXbase项目）
 * es查询的一些方法
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */

@Repository
public class EsQueryDao {
    Logger log = LoggerFactory.getLogger(EsQueryDao.class);

    @Autowired
    TransportClient client;


    /**
     * 处理前端的数据
     * @param map
     * @return
     */
    public Map<String,Object> dealData(Map<String,Object> map){
        int page_size = map.containsKey("page_size") ? (int) map.get("page_size") : 10;
        int page = map.containsKey("page") ? (int)map.get("page") : 0;
        String keywords = map.containsKey("keywords") ? String.valueOf(map.get("keywords")) : "";
        String queryField = map.containsKey("queryField") ? (String) map.get("queryField") : "";//查询的字段
        String[] sortList = map.containsKey("sortList")? (String[]) map.get("sortList") : null;
        String querymode = map.containsKey("querymode") ? (String) map.get("querymode") : "fuzzy";
        Map<String,Object> map1=new HashMap<>();
        map1.put("page_size",page_size);
        map1.put("page",page);
        map1.put("keywords",keywords);
        map1.put("queryField",queryField);
        map1.put("sortList",sortList);
        map1.put("querymode",querymode);
        return map1;
    }


    /**
     * 全部匹配查询
     *
     * @param client
     * @param index
     * @return SearchHits
     */
    public SearchHits matchAllQuery(TransportClient client, String index) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = client.prepareSearch(index)
                .setQuery(queryBuilder)
                .setSize(10000)
                .get();
        SearchHits searchHits = searchResponse.getHits();
        return searchHits;
    }

    /**
     * 全部匹配查询
     *
     * @param client
     * @param index
     * @return SearchHits
     */
    public SearchHits matchAllQueryByPaper(TransportClient client, String index,int pageNum,int pageSize,SortBuilder sortBuilder) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse= null;
        if (sortBuilder!=null){
            searchResponse = client.prepareSearch(index)
                    .setQuery(queryBuilder)
                    .setFrom(pageNum)
                    .setSize(pageSize)
                    .addSort(sortBuilder)
                    .get();

        }else {
            searchResponse = client.prepareSearch(index)
                    .setQuery(queryBuilder)
                    .setFrom(pageNum)
                    .setSize(pageSize)
                    .get();
        }
        SearchHits searchHits = searchResponse.getHits();
        return searchHits;
    }

    /**
     * 判断索引是否存在 传入参数为索引名称
     *
     * @param client
     * @param indexName
     * @return true:存在 false:不存在
     * elastissarch的版本（pom）6.4.3
     */
    public boolean isIndexExists(TransportClient client, String indexName) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * 删除索引
     *
     * @param client
     * @param indexName
     * @return true:删除成功 false:删除失败
     * elastissarch的版本（pom）6.4.3
     */
//    public boolean deleteIndex(TransportClient client, String indexName) {
//        if (isIndexExists(client, indexName)) {
//            DeleteIndexResponse dResponse = (DeleteIndexResponse) client.admin()
//                    .indices().prepareDelete(indexName).execute().actionGet();
//            if (dResponse.isAcknowledged()) {
//                log.info("删除索引 " + indexName + "  成功!");
//                return true;
//            } else {
//                log.info("删除索引 " + indexName + "  失败!");
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }

    /**
     * es中存储的数组重新转成String[]
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id        数组的字段id
     * @param field     数组的字段名字
     * @returnString[]
     */
    public String[] queryArray(TransportClient client, String indexName, String typeName, String id, String field) {
        GetResponse response = client.prepareGet(indexName, typeName, id).get();
        Map<String, Object> map = response.getSourceAsMap();
        log.info(String.valueOf(map.get(field)));
        String tags = String.valueOf(map.get(field));//获取es的数组转成String[]数组
        tags = Util.removeBrackets(tags);
        log.info("tags:" + tags);
        String[] sz = tags.split(",");//查出来的重新转成数组
        return sz;
    }

    /**
     * id记录表的创建
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id
     * @return boolean true:创建成功  fasle:创建失败
     * @throws IOException
     */
    public boolean idTempInsert(TransportClient client, String indexName, String typeName, String id) throws IOException {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", id)
                .endObject();
        IndexResponse indexResponse = client.prepareIndex(indexName, typeName, id)
                .setSource(xContentBuilder).get();
        if (indexResponse.status().equals("CREATED")) {
            return true;
        } else
            return false;
    }

    /**
     * 查询数据所有的id
     *
     * @return list<String>
     */
    public List<String> idList(TransportClient client, String indexName) {
        SearchHits hits = matchAllQuery(client, indexName);
        List<String> list = new ArrayList<>();
        hits.forEach(i -> {
            list.add(i.getId());
        });
        return list;
    }

    /**
     * id记录表的存在查询
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id
     * @return boolean true:存在 fasle:不存在
     */
    public boolean idTempQuery(TransportClient client, String indexName, String typeName, String id) {
        GetResponse getResponse = client.prepareGet(indexName, typeName, id).get();
        return getResponse.isExists();
    }

    /**
     * 查询某个字段是否存在
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id
     * @param field
     * @return boolean true:存在 fasle:不存在
     */
    public boolean fieldQueryExist(TransportClient client, String indexName, String typeName, String id, String field) {
        GetResponse getResponse = client.prepareGet(indexName, typeName, id).get();
        Map<String, Object> map = getResponse.getSourceAsMap();
        if (map.get(field) != null) {
            return true;
        } else
            return false;
    }

    /**
     * 查询某个字端的值是否正确
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id
     * @param field
     * @return boolean true:正确 fasle:不正确
     */
    public boolean fieldQueryMatch(TransportClient client, String indexName, String typeName, String id, String field, String value) {
        GetResponse getResponse = client.prepareGet(indexName, typeName, id).get();
        Map<String, Object> map = getResponse.getSourceAsMap();
        if (map.get(field).equals(value)) {
            return true;
        } else
            return false;
    }

    /**
     * 更新es中索引的单个字段的值
     *
     * @param client
     * @param indexName
     * @param typeName
     * @param id
     * @return Boolean boolean true:修改成功  fasle:修改失败
     */
    public boolean updateField(TransportClient client, String indexName, String typeName, String id, String field, String value) {
        UpdateRequest updateRequest = new UpdateRequest();
        try {
            updateRequest.index(indexName)
                    .type(typeName)
                    .id(id)
                    .doc(
                            XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field(field, value)
                                    .endObject()
                    );
            UpdateResponse response = client.update(updateRequest).get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.warn(id + " 更新异常！");
            return false;
        }
        return true;
    }

    /**
     * 转移A索引的一个字段到B索引
     *
     * @param client
     * @param sourceIndexName
     * @param sourceTypeName
     * @param targetIndexName
     * @param targetTypeName
     * @param targetField     目标字段的名称
     * @param sourceField     源字段名称
     * @return List<String>
     */
    public List<String> transportField(TransportClient client, String sourceIndexName, String sourceTypeName,
                                       String targetIndexName, String targetTypeName, String sourceField, String targetField) {
        SearchHits searchHits = matchAllQuery(client, sourceIndexName);
        List<String> list = new ArrayList<>();
        for (SearchHit i : searchHits) {
            Map<String, Object> map = i.getSourceAsMap();
            try {
                XContentBuilder xContentBuilder = XContentFactory
                        .jsonBuilder()
                        .startObject()
                        .field(targetField, String.valueOf(map.get(sourceField)))
                        .endObject();
                IndexResponse indexResponse = client.prepareIndex(targetIndexName, targetTypeName, i.getId())
                        .setSource(xContentBuilder).get();
                list.add(i.getId());
            } catch (IOException e) {
                log.warn("id转移失败");
                e.printStackTrace();
            }
        }
        log.info("id转移成功");
        return list;
    }

    /**
     * 获得客户端的配置
     *
     * @return
     * @throws UnknownHostException
     */
    public TransportClient client(String clusterName, String clusterIp) throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        TransportAddress transportAddress = new TransportAddress(
                InetAddress.getByName(clusterIp), 9300
        );
        client.addTransportAddress(transportAddress);
        return client;
    }

    /**
     * 排序(不分词)
     * @param map
     * @return
     */
    public SortBuilder getSortBuilder(Map map){
        SortBuilder sortBuilder = null;
        if (map.get("dataType").equals("string")){
            if (map.get("Order").equals(true)){
                sortBuilder = SortBuilders.fieldSort(String.valueOf(map.get("Sort"))).order(SortOrder.DESC);
            }else {
                sortBuilder = SortBuilders.fieldSort(String.valueOf(map.get("Sort"))).order(SortOrder.ASC);
            }
        }else{
            if (map.get("Order").equals(true)){
                sortBuilder = SortBuilders.fieldSort(String.valueOf(map.get("Sort"))).order(SortOrder.DESC);
            }else {
                sortBuilder = SortBuilders.fieldSort(String.valueOf(map.get("Sort"))).order(SortOrder.ASC);
            }
        }
        return sortBuilder;
    }

    /**
     * 查询一个字段（分页，排序）
     * @param client
     * @param index
     * @param field
     * @param value
     * @param pageNum
     * @param pageSize
     * @param sortBuilder
     * @return
     */
    public SearchHits getOneFieldQuery(TransportClient client, String index, String field,String value,
                                       int pageNum,int pageSize,SortBuilder sortBuilder){
        SearchResponse searchResponse;
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,value);
        if (sortBuilder == null){
            searchResponse = client.prepareSearch(index)
                    .setQuery(queryBuilder)
                    .setFrom(pageNum)
                    .setSize(pageSize)
                    .get();
        }else {
            searchResponse = client.prepareSearch(index)
                    .setQuery(queryBuilder)
                    .setFrom(pageNum)
                    .setSize(pageSize)
                    .addSort(sortBuilder)
                    .get();
        }
        return searchResponse.getHits();
    }
}
