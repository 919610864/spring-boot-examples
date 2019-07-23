package com.neo.springlisten;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 抽象服务
 * Created by aa on 2017-08-23.
 */
public abstract class AbstractService implements Service {

    public static final int CREATED = 0;
    public static final int INITIALIZED = 1;
    public static final int RUNNING = 2;
    public static final int PAUSED = 3;
    public static final int STOPED = 4;
    public static final int DESTROYED = 5;

    private static Logger logger = LoggerFactory.getLogger(AbstractService.class);
    private String serviceClassName = this.getClass().getCanonicalName();
    private int status = 0;
    private ServiceLite serviceLite;

    public AbstractService getType() {
        return this;
    }

    public final ServiceLite getServiceLite() {
        if (serviceLite == null) {
            synchronized (this) {
                if (serviceLite == null) {
                    serviceLite = this.getClass().getAnnotation(ServiceLite.class);
                    if (!StringUtils.hasText(serviceClassName)) {
                        serviceClassName = this.getClass().getCanonicalName();
                    }
                    if (serviceLite == null) {
                        serviceLite = this.getClass().getSuperclass().getAnnotation(ServiceLite.class);
                        serviceClassName = this.getClass().getSuperclass().getCanonicalName();
                    }
                    if (serviceLite == null) {
                        logger.error("服务未标注@ServiceLite，请检查：{}", serviceClassName);
                        Runtime.getRuntime().exit(1);
                    }
                }
            }
        }
        return serviceLite;
    }

    public final String getSafeName() {
        if (StringUtils.hasText(getServiceLite().name())) {
            return getServiceLite().name();
        }
        return getServiceClassName();
    }

    public final String getFullName() {
        if (StringUtils.hasText(getServiceLite().name())) {
            return getServiceLite().name() + "(" + getServiceClassName() + ")";
        }
        return getSafeName();
    }

    public final void onInitialize() {
        if (status == INITIALIZED) {
            return;
        }
        if (status == CREATED || status == DESTROYED) {
            synchronized (this) {
                if (status == CREATED || status == DESTROYED) {
                    logger.info("开始初始化服务：" + getFullName());
                    this.initialize();
                    status = INITIALIZED;
                    logger.info("已初始化服务：" + getFullName());
                }
            }
        }
    }

    public final void onStart() {
        if (status == RUNNING) {
            return;
        }
        String autoStartEnabled = System.getenv("hivescm.serviceLite.autoStartEnabled");
        if ("false".equals(autoStartEnabled)) {
            logger.info("系统环境变量hivescm.serviceLite.autoStartEnabled=false，不启动服务：" + getFullName());
            return;
        }
        checkForStart();
        synchronized (this) {
            checkForStart();
            logger.info("开始启动服务：" + getFullName());
            this.start();
            status = RUNNING;
            logger.info("已启动服务：" + getFullName());
        }
    }

    public final void onPause() {
        if (status == PAUSED) {
            return;
        }
        checkForPause();
        synchronized (this) {
            checkForPause();
            logger.info("开始暂停服务：" + getFullName());
            this.pause();
            status = PAUSED;
            logger.info("已暂停服务：" + getFullName());
        }
    }

    public final void onResume() {
        if (status == RUNNING) {
            return;
        }
        checkForResume();
        synchronized (this) {
            checkForResume();
            logger.info("开始恢复服务：" + getFullName());
            this.resume();
            status = RUNNING;
            logger.info("已恢复服务：" + getFullName());
        }
    }

    public final void onStop() {
        if (status == STOPED) {
            return;
        }
        checkForStop();
        synchronized (this) {
            checkForStop();
            logger.info("开始停止服务：" + getFullName());
            this.stop();
            status = STOPED;
            logger.info("已停止服务：" + getFullName());
        }
    }

    public final void onDestroy() {
        if (status == DESTROYED) {
            return;
        }
        logger.info("开始销毁服务：" + getFullName());
        this.destroy();
        status = DESTROYED;
        logger.info("已销毁服务：" + getFullName());
    }

    private void checkForStart() {
        if (status != INITIALIZED && status != STOPED) {
            throw new UnsupportedOperationException("服务未初始化或不在停止状态：" + getSafeName());
        }
    }

    private void checkForPause() {
        if (!getServiceLite().canPause()) {
            throw new UnsupportedOperationException("服务不可暂停：" + getSafeName());
        } else if (status != RUNNING) {
            throw new UnsupportedOperationException("服务尚未在运行：" + getSafeName());
        }
    }

    private void checkForResume() {
        if (!getServiceLite().canPause()) {
            throw new UnsupportedOperationException("服务不可暂停：" + getSafeName());
        } else if (status != PAUSED) {
            throw new UnsupportedOperationException("服务不在暂停状态：" + getSafeName());
        }
    }

    private void checkForStop() {
        if (status != PAUSED && status != RUNNING) {
            throw new UnsupportedOperationException("服务不在运行或暂停状态：" + getSafeName());
        }
    }

    private void checkForDestroy() {
        if (status != INITIALIZED && status != PAUSED && status != STOPED) {
            throw new UnsupportedOperationException("服务未初始化或不在暂停也不在停止状态：" + getSafeName());
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    public final String getServiceClassName() {
        return this.serviceClassName;
    }

    public int getStatus() {
        return this.status;
    }

    private void setStatus(int status) {
        this.status = status;
    }
}
