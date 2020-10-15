package com.neo.controller.delay;

import com.neo.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/delayBookController")
@Api(value = "DelayBookController",tags = {"死信队列"})
public class DelayBookController {

    @Autowired
    private BookService bookService;


    @ApiOperation(value = "死信队列demo", notes = "死信队列demo")
    @GetMapping("/defaultMessage")
    public void defaultMessage() {

        bookService.sendDelayQueue();

    }
}
