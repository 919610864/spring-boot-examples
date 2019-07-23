package com.neo.springlisten;

import com.neo.paralle.Task;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Configuration
public class TestApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(TestApplicationListener.class);
    private Timer timer = new Timer();
    private ApplicationContext applicationContext;


    /**
     * 服务集合
     */
    private final Set<AbstractService> services = new CopyOnWriteArraySet<>();

    /**
     * 实例化服务管理器
     */
    public TestApplicationListener() {
    }

    public AbstractService getService(String name) throws NotFoundException {
        List<AbstractService> foundServiceList = services.stream().filter(s -> s.getSafeName().equals(name)).collect(Collectors.toList());
        if (foundServiceList == null || foundServiceList.size() < 1) {
            throw new NotFoundException("未找到服务：" + name);
        } else {
            return foundServiceList.get(0);
        }
    }

    public List<MicroService> getServiceList(MicroService microService) {
        List<MicroService> microServiceList = new ArrayList<>(services.size());
        for (AbstractService service : services) {
            MicroService microServiceItem = new MicroService();
            microServiceItem.setName(service.getSafeName());
            microServiceItem.setClassName(service.getServiceClassName());
            microServiceItem.setStatus(service.getStatus());
            microServiceList.add(microServiceItem);
        }
        return microServiceList;
    }

    public void startService(MicroService microService) throws NotFoundException {
        getService(microService.getName()).onStart();
    }

    public void pauseService(MicroService microService) throws NotFoundException {
        getService(microService.getName()).onPause();
    }

    public void resumeService(MicroService microService) throws NotFoundException {
        getService(microService.getName()).onResume();
    }

    public void stopService(MicroService microService) throws NotFoundException {
        getService(microService.getName()).onStop();
    }

    private void doInitialize() {
        if (this.services.size() > 0) {
            return;
        }
        Map<String, AbstractService> beans = applicationContext.getBeansOfType(AbstractService.class);
        this.services.addAll(beans.values());
        if (services.size() < 1) {
            return;
        }
        logger.info("服务系统初始化");
        checkDuplicatedName();
        for (AbstractService service : services) {
            try {
                if (AbstractMQListener.class.isAssignableFrom(service.getClass())) {
                    AbstractMQListener abstractMQListener = (AbstractMQListener) service;
                    Field applicationContextField = AbstractMQListener.class.getDeclaredField("applicationContext");
                    Object appContext = applicationContextField.get(abstractMQListener);
                    if (appContext == null) {
                        applicationContextField.set(abstractMQListener, applicationContext);
                    }
                }
            } catch (Throwable throwable) {
                // 尝试修复
                logger.error("尝试修复applicationContext出错", throwable);
            }
            try {
                ServiceLite serviceLite = service.getServiceLite();
                service.onInitialize();
                if (!serviceLite.autoStartup()) {
                    continue;
                }
                if (serviceLite.delaySeconds() < 1) {
                    service.onStart();
                } else {
                    logger.info("服务{}将延迟到{}秒后启动", serviceLite.name(), serviceLite.delaySeconds());
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            service.onStart();
                        }
                    }, serviceLite.delaySeconds() * 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shutdown() {
        logger.info("服务系统关闭");
        for (AbstractService service : services) {
            try {
                service.stop();
                service.destroy();
            } catch (Exception e) {
                //e.printStackTrace();
                logger.error("停止服务异常：", e);
            }
        }
    }

    private void checkDuplicatedName() {
        try {
            List<String> duplicatedNameList = new ArrayList<>();
            services.stream()
                    .collect(Collectors.groupingBy(AbstractService::getSafeName, Collectors.toList()))
                    .forEach((name, list) -> {
                        if (list != null && list.size() > 1) {
                            duplicatedNameList.add(name);
                            logger.error("服务名：{}", name);
                        }
                    });
            if (duplicatedNameList.size() > 0) {
                logger.error("以上服务名存在重复配置，请检查");
                Runtime.getRuntime().exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        Task.start(() -> doInitialize());
        System.out.println("ServiceManager before.................................");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
        System.out.println("ServiceManager after.................................");
    }
}
