package com.neo.service.impl;

import com.neo.model.Book;
import com.neo.rabbit.delay2.ImmediateSender;
import com.neo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: MaGuoDong
 * @Date: Created in 2018/11/7 11:09
 * @company GEGE
 */
@Service
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

//    @Autowired
//    private BookSender bookSender;

    @Autowired
    private ImmediateSender immediateSender;

    @Override
    public void sendDelayQueue() {
//        Book book = new Book();
//        book.setId("1");
//        book.setName("Rabbit in action");
//        bookSender.send(book);
//        log.info("[发送时间] - [{}]", LocalDateTime.now());
    }

    @Override
    public void sendDelayQueue2(int delayTime) {
        Book book = new Book();
        book.setId("2");
        book.setName("Rabbitmq in action");
        immediateSender.send(book,delayTime);
    }
}
