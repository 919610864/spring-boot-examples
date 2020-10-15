//package com.neo.rabbit.direct;
//
//import com.alibaba.fastjson.JSON;
//import com.neo.config.Constants;
//import com.neo.util.JsonUtils;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//
//@Component
//@RabbitListener(queues = Constants.RED_QUEUE_COLOUR)
//public class DirectReceiver2 {
//
//
//    @RabbitHandler
//    public void process(String obj, Channel channel, Message message) throws IOException {
//        System.out.println("Direct Receiver2  : " + JSON.parse(message.getBody()));
//        //Book book = JsonUtils.parseStringDouble(message.getBody(),Book.class);
//        Book book1 = (Book) JSON.parse(message.getBody());
//
//        try {
//            Thread.sleep(5000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//    }
//}
