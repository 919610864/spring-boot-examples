//package com.neo.rabbit.delay;
//
//import com.neo.config.RabbitConfig;
//import com.neo.model.Book;
//import com.rabbitmq.client.Channel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//
///**
// * @Description:
// * @Author: MaGuoDong
// * @Date: Created in 2018/11/6 11:08
// * @company GEGE
// */
//@Component
//public class BookQueueHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(BookQueueHandler.class);
//
//    @RabbitListener(queues = {RabbitConfig.REGISTER_QUEUE_NAME})
//    public void listenerDelayQueue(Book book, Message message, Channel channel) throws IOException {
//        log.info("[listenerQueue监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), book.toString());
//        try {
//            // TODO 通知 MQ 消息已被成功消费,可以ACK了
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            System.out.println("消费成功，通知MQ");
//        } catch (Exception e) {
//            // TODO 如果报错了,那么我们可以进行容错处理,比如转移当前消息进入其它队列
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
//            System.out.println("receiver fail");
//        }
//    }
//
//}
