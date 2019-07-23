package com.neo.springlisten;

public interface MessageReceiveFilter {
    void doFilter(String body);
}
