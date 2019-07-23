package com.neo.paralle;

/**
 * 任务状态
 * Created by xiexindong on 2017-08-21.
 */
public enum TaskStatus {
    /**
     * 创建
     */
    CREATED,
    /**
     * 已排队
     */
    QUEUED,
    /**
     * 正在执行
     */
    RUNNING,
    /**
     * 完成
     */
    FINISHED
}
