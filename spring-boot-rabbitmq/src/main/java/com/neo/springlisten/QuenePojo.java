package com.neo.springlisten;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhangxiaoping
 * @ClassName: QuenePojo
 * @Description: 消息队列传递实体
 * @date 2017年8月22日
 */

public class QuenePojo<T> implements Serializable {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 4519862532473082094L;

    /**
     * 消息id
     */
    private Long msgId;
    /**
     * 数据类型
     */
    private Integer type;
    /**
     * 传递对象
     */
    private T context;

    /**
     * 消息发送次数
     */
    private Integer sendQty = 0;

    /**
     * 延迟发送时间(秒)
     */
    private Integer delaySeconds;

    private List<HeaderItem> headerList;

    /**
     * 是否序列化null
     */
    private boolean serializeNulls;
    /**
     * 辅助信息
     */
    private Map<String,Object> param = new HashMap<String,Object>();

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    public Integer getSendQty() {
        return sendQty;
    }

    public void setSendQty(Integer sendQty) {
        this.sendQty = sendQty;
    }

    public Integer getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(Integer delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public List<HeaderItem> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<HeaderItem> headerList) {
        this.headerList = headerList;
    }

    public boolean isSerializeNulls() {
        return serializeNulls;
    }

    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
