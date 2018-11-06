package com.neo.controller;

import com.neo.rabbit.hello.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: MaGuoDong
 * @Date: Created in 2018/11/1 16:05
 * @company 蜂网供应链管理（上海）有限公司
 */
@RestController
public class TestController {

    @Autowired
    private HelloSender helloSender;

    /**
     * 单生产者-单个消费者
     */
    @RequestMapping("/test")
    public void hello() throws Exception {
        helloSender.send();
    }

}
