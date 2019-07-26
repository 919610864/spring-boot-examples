package com.neo.controller.delay;

import com.neo.model.Book;
import com.neo.rabbit.delay2.ImmediateSender;
import com.neo.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/delayBookController")
public class DelayBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ImmediateSender immediateSender;


    @ApiOperation(value = "延迟队列demo", notes = "延迟队列demo")
    @GetMapping("/defaultMessage")
    public void defaultMessage() {
        bookService.sendDelayQueue();
    }

    @ApiOperation(value = "延迟队列demo", notes = "延迟队列demo")
    @GetMapping("/defaultMessage2/{delayTime}")
    public void defaultMessage2(@PathVariable("delayTime") int delayTime){
        bookService.sendDelayQueue2(delayTime);
    }
}
