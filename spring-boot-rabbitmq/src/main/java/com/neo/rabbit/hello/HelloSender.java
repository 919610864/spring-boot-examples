package com.neo.rabbit.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender{

	private Logger logger = LoggerFactory.getLogger(HelloSender.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		//User user = new User("mgd","mgd");
		String context = "你好现在是 " + new Date() +"";
		logger.info("HelloSender发送内容 : {}",context);
		//this.rabbitTemplate.setReturnCallback(this);
//		this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//			if (!ack) {
//				System.out.println("HelloSender消息发送失败" + cause + correlationData.toString());
//			} else {
//				System.out.println("HelloSender 消息发送成功 ");
//			}
//		});
		this.rabbitTemplate.convertAndSend("hello", context);
	}


//	@Override
//	public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//		System.out.println("sender return success" + message.toString()+"==="+i+"==="+s1+"==="+s2);
//	}


//    @Override
//    public void confirm(CorrelationData correlationData, boolean b, String s) {
//        System.out.println("sender success");
//    }

}