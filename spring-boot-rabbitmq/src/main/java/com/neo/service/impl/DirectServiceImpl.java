package com.neo.service.impl;

import com.neo.rabbit.direct.DirectSender;
import com.neo.service.DirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectServiceImpl implements DirectService {

    @Autowired
    private DirectSender directSender;

    @Override
    public void send(String routingKey) {
        directSender.send(routingKey);
    }
}
