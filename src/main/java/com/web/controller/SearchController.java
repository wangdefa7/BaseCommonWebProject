package com.web.controller;

import com.web.core.CommonResult;
import com.web.core.ResMessage;
import com.web.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *查询数据前段接口类（XXbase项目）
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */

@CrossOrigin
@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    /**
     *
     * @apiName list
     * @api {POST} ：8089/list  查询XX数据的接口
     * @apiDescription 查询XX数据的接口
     * @apiVersion 1.0.1
     * @apiParam   {String} startDate 时间(格式：yyyy-mm-dd)
     * @apiParam   {String} endDate 时间(格式：yyyy-mm-dd)
     * @apiParam   {int} pageSize 页面大小
     * @apiParam   {String} [rip] 响应IP，或者[sip],源IP   可选参数
     * @apiSuccess {int} code 返回码
     * @apiSuccess {String} message  返回消息
     * @apiSuccess {List} data  返回数据
     * @apiParamExample 请求样例
     * {
     * 	"pageSize":10,
     * 	"pageNum":1,
     * 	"domain":"2018.ip138.com",
     * 	"startDate":"",
     * 	"endDate":"",
     * 	"rip":"",
     * 	"sip":"",
     * 	"sortList":[]
     * }
     *
     * @apiSuccessExample {json} Success-Response：
     * {
     *     "code": 1000,
     *     "message": "succ",
     *     "data": [
     *         {
     *             "qcc": "中国山东省枣庄市电信",
     *             "dt": "2018-09-20",
     *             "value_type": "sip",
     *             "num": 1,
     *             "rk": "ws-pclivemate.pull.yximgs.com",
     *             "value": "113.123.218.53"
     *         }
     *     ],
     *     "total": 1,
     *     "mergeData": null
     * }
     *@apiError (Error) {String} message 查询失败
     *
     *@apiErrorExample {json} 失败返回样例
     * {
     *     "code": 1,
     *     "message": "查询失败",
     *     "data": null
     * }
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResMessage list(@RequestBody Map map){
        CommonResult commonResult = searchService.list(map);
        return ResMessage.genSucessMessage("",commonResult.list);
    }
}
