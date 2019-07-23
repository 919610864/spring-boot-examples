package com.neo.springlisten;

import java.lang.annotation.*;

/**
 * 消息队列配置
 * Created by aa on 2017-08-24.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MQListener {
    /**
     * 交换机
     *
     * @return
     */
    String exchange() default "exchange.wms";

    /**
     * 路由
     *
     * @return
     */
    String routingKey() default "";

    /**
     * 队列
     *
     * @return
     */
    String queue() default "";

    Class targetClass() default String.class;

    boolean multipleNode() default false;
}
