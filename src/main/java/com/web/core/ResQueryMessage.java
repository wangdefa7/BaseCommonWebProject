package com.web.core;

public class ResQueryMessage extends ResMessage {

    public int count;

    public int total;

    /**
     * 生成成功消息
     * @param message 操作详细
     * @param data 数据包
     * @return
     */
    public static ResQueryMessage genSucessMessage(String message, Object data, int count, int total){
        ResQueryMessage resMessage = new ResQueryMessage();

        resMessage.code = CodeDict.Success;
        resMessage.message = message;
        resMessage.data = data;
        resMessage.count = count;
        resMessage.total = total;

        return resMessage;
    }


    /**
     * 生成成功消息
     * @param message 操作详细
     * @param data 数据包
     * @return
     */
    public static ResQueryMessage genSucessMessage(String message, Object data, Object mergeData, int count, int total){
        ResQueryMessage resMessage = new ResQueryMessage();

        resMessage.code = CodeDict.Success;
        resMessage.message = message;
        resMessage.data = data;
        resMessage.mergeData = mergeData;
        resMessage.count = count;
        resMessage.total = total;

        return resMessage;
    }
}
