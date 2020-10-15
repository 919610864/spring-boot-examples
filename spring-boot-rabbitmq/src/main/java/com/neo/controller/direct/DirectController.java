package com.neo.controller.direct;


import com.neo.service.DirectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/directController")
@Api(value = "DirectController",tags = {"完全匹配"})
public class DirectController {

    @Autowired
    private DirectService directService;

    @ApiOperation(value = "完全匹配", notes = "完全匹配")
    @GetMapping("/direct")
    public String direct(String routingKey){
        directService.send(routingKey);
        return "success";
    }
}
