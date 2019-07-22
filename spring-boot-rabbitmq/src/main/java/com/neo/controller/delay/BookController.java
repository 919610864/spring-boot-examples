package com.neo.controller.delay;

import com.neo.config.RabbitConfig;
import com.neo.model.Book;
import com.neo.service.BookService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: MaGuoDong
 * @Date: Created in 2018/11/6 11:04
 * @company GEGE
 */
@RestController
@RequestMapping("/bookController")
public class BookController {

    @Autowired
    private BookService bookService;


    @ApiOperation(value = "死信队列demo", notes = "死信队列demo")
    @GetMapping("/defaultMessage")
    public void defaultMessage() {

        bookService.sendDelayQueue();

    }

}
