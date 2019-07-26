//package com.neo.rabbit.delay;
//
//import com.neo.config.RabbitConfig;
//import com.neo.model.Book;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @Description:
// * @Author: MaGuoDong
// * @Date: Created in 2018/11/7 11:19
// * @company GEGE
// */
//@Component
//public class BookSender{
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    public void send(Book book){
//        // 添加延时队列
//        this.rabbitTemplate.convertAndSend(RabbitConfig.REGISTER_QUEUE_NAME, RabbitConfig.ROUTING_KEY, book, message -> {
//            message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Book.class.getName());
//            message.getMessageProperties().setExpiration(10 * 1000 + "");
//            return message;
//        });
//
//    }
//}
