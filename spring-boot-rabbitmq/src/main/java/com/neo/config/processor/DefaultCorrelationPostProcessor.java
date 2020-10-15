package com.neo.config.processor;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.UUID;

/**
 * @author dingsx
 * @create 2019-11-13 17:20
 **/
public class DefaultCorrelationPostProcessor implements CorrelationPostProcessor {

    private final long ttl;

    private final int priority;



    public DefaultCorrelationPostProcessor(Long ttl, Integer priority) {
        this.ttl = ttl;
        this.priority = priority;
    }


    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        return postProcessMessage(message, null);
    }

    /**
     * 处理消息
     *
     * @param message     消息
     * @param correlation 关联属性
     * @return
     */
    public Message postProcessMessage(Message message, CorrelationData correlation) {
        String correlationId = UUID.randomUUID().toString();
        if (correlation != null) {
            if (correlation instanceof CorrelationData) {
                CorrelationData correlationData = (CorrelationData) correlation;
                correlationId = correlationData.getId();
            }
        }
        message.getMessageProperties().setContentEncoding("UTF-8");
        if (ttl > 0) {
            message.getMessageProperties().setExpiration(String.valueOf(this.ttl));
        }
        if (priority > 0) {
            message.getMessageProperties().setPriority(this.priority);
        }
        //message.getMessageProperties().setCorrelationId(correlationId);
        return message;
    }


}
