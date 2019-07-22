package com.neo.controller.topic;

import com.neo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: MaGuoDong
 * @Date: Created in 2018/11/2 14:46
 * @company 蜂网供应链管理（上海）有限公司
 */
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @RequestMapping("/send")
    public void send(){
        topicService.send();
    }

    @RequestMapping("/send1")
    public void send1(){
        topicService.send1();
    }

    @RequestMapping("/send2")
    public void send2(){
        topicService.send2();
    }

    @RequestMapping("/send3")
    public void send3(){
        topicService.send();
        topicService.send1();
        topicService.send2();
    }
}
