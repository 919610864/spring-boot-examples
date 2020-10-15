package com.neo.rabbit.fanout;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FanoutSender {

	@Value("${server.port}")
	private String port;

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send() {
		String context = "hi, fanout msg "+"端口号为:"+ port;
		System.out.println("Sender : " + context);
		this.rabbitTemplate.convertAndSend("fanoutExchange","123", context);
	}

}