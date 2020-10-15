package com.neo.rabbit.direct;

import com.neo.config.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.ORANGE_QUEUE_COLOUR)
public class DirectReceiver {

    @RabbitHandler
    public void process(String obj, Channel channel, Message message) {
        System.out.println("Direct Receiver1  : " + obj);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
