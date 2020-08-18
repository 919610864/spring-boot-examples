package com.neo.service.impl;

import com.neo.rabbit.fanout.FanoutSender;
import com.neo.service.FanoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: MaGuoDong
 * @Date: Created in 2018/11/2 14:47
 * @company 蜂网供应链管理（上海）有限公司
 */
@Service
public class FanoutServiceImpl implements FanoutService {

    @Autowired
    private FanoutSender fanoutSender;

    @Override
    public void send() {
        fanoutSender.send();
    }

    @Override
    public void send1() {

    }

    @Override
    public void send2() {

    }
}
