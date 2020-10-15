package com.neo.rabbit.delay2;

import com.neo.model.Book;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ImmediateReceiver {

    @RabbitListener(queues = "immediate_queue")
    @RabbitHandler
    public void get(Book booking) {
        System.out.println("收到延时消息了" + booking);
    }
}
