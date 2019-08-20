package com.web.service;


import com.web.core.CommonResult;
import com.web.dao.SearchDao;
import com.web.model.CommonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
/**
 *查询数据Service类（XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
@Service
public class SearchService {

    @Autowired
    SearchDao dao;

    public CommonResult list(Map map){
        return dao.searchEs(map);
    }

    public List searchTwice(Map map, CommonEntity commonEntity){
        return dao.searchTwice(map,commonEntity);
    }


}
