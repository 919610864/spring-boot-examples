package com.neo.rabbit.direct;

import com.alibaba.fastjson.JSON;
import com.neo.config.Constants;
import com.neo.config.processor.CorrelationPostProcessor;
import com.neo.config.processor.DefaultCorrelationPostProcessor;
import com.neo.model.Book;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DirectSender {

	@Autowired
    private RabbitTemplate rabbitTemplate;

	public void send(String routingKey) {
		Book book = new Book();
		book.setId("1");
		book.setName("水壶转");
		String context = "hi, i am direct message all";
		System.out.println("Sender : " + context);
		Long expiration = 0l;
		int priority = 0;
        CorrelationPostProcessor correlationPostProcessor = null;
        if (expiration > 0 || priority > 0) {
                correlationPostProcessor = new DefaultCorrelationPostProcessor(expiration, priority);
        } else {
            if (correlationPostProcessor == null) {
                correlationPostProcessor = new DefaultCorrelationPostProcessor(expiration, priority);
            }
        }
        String tagId = null;
        if (tagId == null) {
            tagId = UUID.randomUUID().toString();
        }
        CorrelationData correlationData = new CorrelationData(tagId);
        rabbitTemplate.convertAndSend(Constants.DIRECT_EXCHANGE, routingKey, JSON.toJSONString(book), correlationPostProcessor, correlationData);
	}

}