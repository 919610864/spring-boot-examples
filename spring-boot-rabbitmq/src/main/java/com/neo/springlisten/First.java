package com.neo.springlisten;

public class First extends AbstractMQListener {


    @Override
    public void onMessage(MessageInfo messageInfo) {
        System.out.println(messageInfo.getBody());
    }
}
