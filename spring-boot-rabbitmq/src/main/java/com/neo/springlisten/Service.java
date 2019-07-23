package com.neo.springlisten;

/**
 * 服务
 * Created by aa on 2017-08-23.
 */
public interface Service {

    /**
     * 初始化服务
     */
    void initialize();

    /**
     * 启动服务
     */
    void start();

    /**
     * 暂停服务
     */
    void pause();

    /**
     * 恢复服务
     */
    void resume();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 销毁服务
     */
    void destroy();
}
