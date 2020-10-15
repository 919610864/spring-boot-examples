package com.neo.controller.fanout;


import com.neo.service.FanoutService;
import com.neo.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fanout")
@Api("广播")
public class FanoutController {


    @Autowired
    private FanoutService fanoutService;

    @ApiOperation(value = "广播模式", notes = "广播模式")
    @RequestMapping("/send")
    public void send(){
        fanoutService.send();
    }
}
