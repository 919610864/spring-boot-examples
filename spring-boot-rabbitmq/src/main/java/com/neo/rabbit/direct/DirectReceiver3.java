package com.neo.rabbit.direct;

import com.alibaba.fastjson.JSON;
import com.neo.config.Constants;
import com.nicetuan.basic.tool.json.JsonUtils;
import com.nicetuan.basic.tool.rabbitmq.handler.DefaultConsumerHandel;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceiver3 extends DefaultConsumerHandel implements ChannelAwareMessageListener {

    @RabbitListener(queues = Constants.RED_QUEUE_COLOUR)
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("Direct Receiver2  : " + JSON.parse(message.getBody()));
        Book book = JsonUtils.parseStringDouble(message.getBody(),Book.class);
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
