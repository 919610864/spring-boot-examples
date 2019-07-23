package com.neo.springlisten;


import com.google.gson.Gson;
import com.neo.util.GsonUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息监听服务
 *
 * @author xiexindong
 * @date 2017-08-24
 */
public abstract class AbstractMQListener extends AbstractService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMQListener.class);
    private String serviceClassName = this.getClass().getCanonicalName();
    private MQListener mqListener;
    private Gson gson;
    SimpleMessageListenerContainer listenerContainer;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ConnectionFactory connectionFactory;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    DirectExchange defaultExchange;
    @Autowired
    TopicExchange topicExchange;
    @Autowired
    DirectExchange defaultExchangeDelay;
    @Autowired
    TopicExchange topicExchangeDelay;

    @Autowired
    ApplicationContext applicationContext;
//    @Autowired
//    private MqLogUtilService mqLogUtilService;

    @Value("${spring.cloud.client.ipAddress:NULL}")
    private String ip;

    private String ipHash;

    /**
     * 当有消息到达时
     *
     * @param messageInfo
     */
    public abstract void onMessage(MessageInfo messageInfo);

    @Override
    public final void initialize() {
        super.initialize();
        doInitialize();
        afterInitialize();
    }

    private void doInitialize() {
        gson = GsonUtils.getGson();
        MQListener listener = getMQListener();
        fixAutowired();
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        boolean isTopic = listener.exchange().endsWith(".topic");
        String delayExchange;
        String queueName = listener.queue();
        if (isTopic) {
            rabbitAdmin.declareExchange(topicExchange);
            rabbitAdmin.declareExchange(topicExchangeDelay);
            delayExchange = topicExchange.getName();
            if (listener.multipleNode()) {
                queueName = String.format("%s_%s", queueName, ipHash);
            }
        } else {
            rabbitAdmin.declareExchange(defaultExchange);
            rabbitAdmin.declareExchange(defaultExchangeDelay);
            delayExchange = defaultExchange.getName();
        }
        Queue queue = new Queue(queueName, true);
        rabbitAdmin.declareQueue(queue);

        // 创建需要的延迟队列
        for (int i = 1; i < 7; i++) {
            // 如果不是主题队列，则可以提前创建
            Queue queueDelay = null;
            String delayRoutingKey = listener.routingKey() + "_" + convertToDelaySeconds(i);
            if (!isTopic) {
                Map<String, Object> delayArguments = new HashMap();
                delayArguments.put("x-dead-letter-exchange", delayExchange);
                delayArguments.put("x-dead-letter-routing-key", listener.routingKey());
                queueDelay = new Queue("z.autoDelay." + queueName + "_" + convertToDelaySeconds(i), true, false, false, delayArguments);
                rabbitAdmin.declareQueue(queueDelay);
            }

            if (isTopic) {
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(listener.routingKey()));
                if (queueDelay != null) {
                    rabbitAdmin.declareBinding(BindingBuilder.bind(queueDelay).to(topicExchangeDelay).with(delayRoutingKey));
                }
            } else {
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(defaultExchange).with(listener.routingKey()));
                if (queueDelay != null) {
                    rabbitAdmin.declareBinding(BindingBuilder.bind(queueDelay).to(defaultExchangeDelay).with(delayRoutingKey));
                }
            }
        }

        listenerContainer.setQueues(queue);
        listenerContainer.setExposeListenerChannel(true);
        listenerContainer.setMaxConcurrentConsumers(1);
        listenerContainer.setConcurrentConsumers(1);
        listenerContainer.setPrefetchCount(1);
        //设置确认模式手工确认
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        listenerContainer.setMessageListener((ChannelAwareMessageListener) (message, channel) -> handleMessage(message, channel));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> destroyContainer()));
    }

    private void fixAutowired() {
        if (applicationContext != null) {
            if (connectionFactory == null) {
                connectionFactory = (ConnectionFactory) applicationContext.getBean("connectionFactory");
            }
            if (defaultExchange == null) {
                defaultExchange = (DirectExchange) applicationContext.getBean("defaultExchange");
            }
            if (topicExchange == null) {
                topicExchange = (TopicExchange) applicationContext.getBean("topicExchange");
            }
            if (defaultExchangeDelay == null) {
                defaultExchangeDelay = (DirectExchange) applicationContext.getBean("defaultExchangeDelay");
            }
            if (topicExchangeDelay == null) {
                topicExchangeDelay = (TopicExchange) applicationContext.getBean("topicExchangeDelay");
            }
//            if (mqLogUtilService == null) {
//                mqLogUtilService = applicationContext.getBean(MqLogUtilService.class);
//            }
            if (!StringUtils.hasText(ip) || ip.equalsIgnoreCase("NULL")) {
                ip = applicationContext.getEnvironment().getProperty("spring.cloud.client.ipAddress", "127.0.0.1");
            }
            ipHash = convertTextToMD5(ip, true);
        } else {
            if (!StringUtils.hasText(ip)) {
                ip = "127.0.0.1";
            }
            ipHash = ip.replaceAll(".", "_").replaceAll(":", "_");
            logger.error("applicationContext is null", serviceClassName);
        }
    }

    private void handleMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        String messageBody = new String(body);
        Object content = null;
        MQListener listener = getMQListener();
        if (listener.targetClass() != null) {
            if (String.class.equals(listener.targetClass())) {
                content = messageBody;
            } else {
                try {
                    content = gson.fromJson(messageBody, listener.targetClass());
                } catch (Exception e) {
                    logger.error("反序列化消息错误", e);
                }
            }
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setOriginalMessage(message)
                .setChannel(channel)
                .setBody(messageBody)
                .setContent(content);

        Map<String, MessageReceiveFilter> filterMap = applicationContext.getBeansOfType(MessageReceiveFilter.class);
        if (filterMap.size() > 0) {
            for (Map.Entry<String, MessageReceiveFilter> filterEntry : filterMap.entrySet()) {
                try {
                    filterEntry.getValue().doFilter(messageBody);
                } catch (Throwable throwable) {
                    logger.error("消息执行过滤器出错", throwable);
                }
            }
        }
        try {
            onMessage(messageInfo);
        } catch (Throwable throwable) {
            logger.error("消息消费时出现未捕获的异常:{}", throwable);
            messageInfo.basicAck(false, throwable);
        }
    }

    public void afterInitialize() {

    }

    @Override
    public final void start() {
        if (listenerContainer != null) {
            listenerContainer.start();
        }
    }

    private MQListener getMQListener() {
        if (mqListener == null) {
            synchronized (this) {
                if (mqListener == null) {
                    mqListener = this.getClass().getAnnotation(MQListener.class);
                    if (mqListener == null) {
                        mqListener = this.getClass().getSuperclass().getAnnotation(MQListener.class);
                    }
                    if (mqListener == null) {
                        logger.error("消息监听服务未标注@MQListener，请检查：{}", serviceClassName);
                        Runtime.getRuntime().exit(1);
                    } else if (!StringUtils.hasText(mqListener.exchange())
                            || !StringUtils.hasText(mqListener.routingKey())
                            || !StringUtils.hasText(mqListener.queue())) {
                        logger.error("消息监听服务未正确配置exchange(不配置则可使用默认的exchange.wms)、routingKey或queue，请检查：{}", serviceClassName);
                        Runtime.getRuntime().exit(1);
                    }
                }
            }
        }
        return mqListener;
    }

    @Override
    public final void stop() {
        if (listenerContainer != null) {
            listenerContainer.stop();
        }
    }

    @Override
    public final void pause() {

    }

    @Override
    public final void resume() {

    }

    public void beforeDestroy() {

    }

    @Override
    public final void destroy() {
        try {
            beforeDestroy();
        } finally {
            super.destroy();
            destroyContainer();
        }
    }

    private void destroyContainer() {
        if (listenerContainer != null) {
            try {
                listenerContainer.stop();
            } catch (Exception e) {
            } finally {
                try {
                    listenerContainer.destroy();
                } catch (Exception e) {
                } finally {

                }
            }
        }
    }

    private static String convertTextToMD5(String plainText, boolean hex) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            if (hex) {
                // 16位的加密
                return buf.toString().substring(8, 24);
            } else {
                // 32位加密
                return buf.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public class MessageInfo {
        private Message originalMessage;
        private String body;
        private Object content;
        private Channel channel;

        public Message getOriginalMessage() {
            return originalMessage;
        }

        private MessageInfo setOriginalMessage(Message originalMessage) {
            this.originalMessage = originalMessage;
            return this;
        }

        public String getBody() {
            return body;
        }

        private MessageInfo setBody(String body) {
            this.body = body;
            return this;
        }

        public Object getContent() {
            return content;
        }

        private MessageInfo setContent(Object content) {
            this.content = content;
            return this;
        }

        public Channel getChannel() {
            return channel;
        }

        private MessageInfo setChannel(Channel channel) {
            this.channel = channel;
            return this;
        }

        /**
         * 确认是否消费成功
         *
         * @param ack 是否消费成功
         */
        public void basicAck(boolean ack) {
            basicAck(ack, null);
        }

        /**
         * 确认是否消费成功
         *
         * @param ack 是否消费成功
         */
        public void basicAck(boolean ack, Throwable ex) {
            try {
                if (ack) {
                    channel.basicAck(originalMessage.getMessageProperties().getDeliveryTag(), false);
                } else {
                    channel.basicNack(originalMessage.getMessageProperties().getDeliveryTag(), false, true);
                }
                saveLog(ex);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("确认消息时出错", e);
                saveLog(e);
            }
        }

        /**
         * 记录日志
         *
         * @param ex
         */
        private void saveLog(Throwable ex) {
//            try {
//                if (gson == null) {
//                    gson = GsonUtils.getGson();
//                }
//                QuenePojo quenePojo;
//                if (content != null && content.getClass() == String.class) {
//                    quenePojo = gson.fromJson(content.toString(), QuenePojo.class);
//                } else {
//                    quenePojo = (QuenePojo) content;
//                }
//                if (quenePojo.getMsgId() == null) {
//                    quenePojo.setMsgId(SnowflakeIdWorker.getWorker().nextId());
//                }
//                MqLog mqLog = new MqLog();
//                mqLog.setMsgId(quenePojo.getMsgId());
//                mqLog.setContent(gson.toJson(quenePojo));
//                mqLog.setQueneType(quenePojo.getType());
//                mqLog.setLogType(2);
//                if (getMQListener() != null) {
//                    mqLog.setRoutingKey(getMQListener().routingKey());
//                    mqLog.setQueneName(getMQListener().queue());
//                }
//                if (ex != null) {
//                    mqLog.setStatus(300);
//                    String errorMsg = null;
//                    try {
//                        errorMsg = printStackTraceToString(ex);
//                        if (errorMsg.length() > 2000) {
//                            errorMsg = errorMsg.substring(0, 1999);
//                        }
//                    } catch (Exception exc) {
//                        errorMsg = ex.getMessage();
//                    }
//                    mqLog.setExceptionMsg(errorMsg);
//                } else {
//                    mqLog.setStatus(200);
//                }
//                //第四次发送并且失败(status == 300),需要将错误信息记录到数据库中
//                try {
//                    // 谢新东：不管是第几次失败均记录到数据库
//                   // mqLogUtilService.saveLog(mqLog);
//                    //if (mqLog.getStatus() == 300 && quenePojo.getSendQty() == 4) {
//                    //    mqLogUtilService.saveLog(mqLog);
//                    //} else if (mqLog.getStatus() != 300) {
//                    //    mqLogUtilService.saveLog(mqLog);
//                    //}
//                } catch (Throwable throwable) {
//                    logger.error("保存mq日志失败", throwable);
//                }
//            } catch (Exception e) {
//                logger.info("mq解析对象失败", content);
//                e.printStackTrace();
//            }
        }

        public String printStackTraceToString(Throwable t) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw, true));
            return sw.getBuffer().toString();
        }
    }


    public static int convertToDelaySeconds(int sendQty) {
        int delaySeconds;
        switch (sendQty) {
            case 0:
                delaySeconds = 0;
                break;
            case 1:
                delaySeconds = 10;
                break;
            case 2:
                delaySeconds = 30;
                break;
            case 3:
                delaySeconds = 60;
                break;
            case 4:
                delaySeconds = 120;
                break;
            case 5:
                delaySeconds = 300;
                break;
            default:
                delaySeconds = 600;
                break;
        }
        return delaySeconds;
    }

}
