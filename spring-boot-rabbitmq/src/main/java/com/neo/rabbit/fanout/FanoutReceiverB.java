package com.neo.rabbit.fanout;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "fanout.B")
public class FanoutReceiverB {

    @Value("${server.port}")
    private String port;

    @RabbitHandler
    public void process(String obj, Channel channel, Message message) throws IOException {
        System.out.println("fanout Receiver B  : " +"port:"+port+ obj);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {

            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
        }
    }

}
