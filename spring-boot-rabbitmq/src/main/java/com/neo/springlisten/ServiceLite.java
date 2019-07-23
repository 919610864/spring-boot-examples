package com.neo.springlisten;

import java.lang.annotation.*;

/**
 * 服务标记
 * Created by aa on 2017-08-24.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceLite {
    /**
     * 服务名
     *
     * @return
     */
    String name() default "";

    /**
     * 服务延迟启动秒数
     *
     * @return
     */

    int delaySeconds() default 0;

    /**
     * 是否自动启动
     *
     * @return
     */

    boolean autoStartup() default true;

    /**
     * 是否可暂停
     *
     * @return
     */
    boolean canPause() default false;
}
