package com.neo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:mgd
 */
@Configuration
public class RabbitConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfig.class);

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }


    /**
     * queue with the default exchange
     *
     * @return
     */
    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    /*** fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。**/
    @Bean
    public Queue AMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }
    /************************************************fanout********************************************************/


    /********************************************** 完全匹配，单播的模式**********************************/
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    /********************************************** 完全匹配，单播的模式**********************************/

    /**
     * topic 交换器通过模式匹配分配消息的路由键属性，将路由键和某个模式进行匹配，此时队列需要绑定到一个模式上。
     * 它将路由键和绑定键的字符串切分成单词，这些单词之间用点隔开。
     * 它同样也会识别两个通配符：符号“#”和符号“”。#匹配0个或多个单词，匹配不多不少一个单词
     *
     * @return
     */

    final static String message = "topic.message";
    final static String messages = "topic.messages";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Queue queueMessage() {
        return new Queue(RabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(RabbitConfig.messages);
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }

    /**************************************topic***************************************************/


    /**
     * rabbitTemplate.convertAndSend(createCustomerQueueName, requestWrapper);来自动序列化成json字符串了
     * @return
     */
//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }

    /*******************************************************延时队列***************************************************************/

    /**
     * //     * 延迟队列 TTL 名称
     * //
     */
    public static final String DELAY_QUEUE = "dev.book.register.delay.queue";
    /**
     * DLX，dead letter发送到的 exchange
     * exchange 很重要,具体消息就是发送到该交换机的
     */
    public static final String DELAY_EXCHANGE = "dev.book.register.delay.exchange";
    /**
     * routing key 名称
     * routingKey 很重要要,具体消息发送在该 routingKey 的
     */
    public static final String DELAY_ROUTING_KEY = "";


    public static final String REGISTER_QUEUE_NAME = "dev.book.register.queue";
    public static final String REGISTER_EXCHANGE_NAME = "dev.book.register.exchange";
    public static final String ROUTING_KEY = "all";

    @Bean
    public Queue delayProcessQueue() {
        Map<String, Object> params = new HashMap<>();
        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        params.put("x-dead-letter-exchange", DELAY_EXCHANGE);
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        params.put("x-dead-letter-routing-key", DELAY_ROUTING_KEY);
        return new Queue(DELAY_QUEUE, true, false, false, params);
    }

    /**
     * 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。
     * 这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog。
     * TODO 它不像 TopicExchange 那样可以使用通配符适配多个
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange delayExchange() {
        return new TopicExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(delayProcessQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }


    @Bean
    public Queue registerBookQueue() {
        return new Queue(REGISTER_QUEUE_NAME, true);
    }

    /**
     * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。
     * 符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。
     **/
    @Bean
    public TopicExchange registerBookTopicExchange() {
        return new TopicExchange(REGISTER_EXCHANGE_NAME);
    }

    @Bean
    public Binding registerBookBinding() {
        return BindingBuilder.bind(registerBookQueue()).to(registerBookTopicExchange()).with(ROUTING_KEY);
    }


    @Bean
    public Queue immediateQueue() {
        return new Queue("immediate_queue", true);
    }

    /**
     * 延迟队列
     *
     * @return
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> params = new HashMap<>();
        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        params.put("x-dead-letter-exchange", "immediate_exchange");
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        params.put("x-dead-letter-routing-key", "order");
        return new Queue("delay_queue", true, false, false, params);

    }

    @Bean
    public DirectExchange immediateExchange() {
        return new DirectExchange("immediate_exchange", true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dead_letter_exchange", true, false);
    }

    @Bean
    public Binding immediateBinding() {
        return BindingBuilder.bind(immediateQueue()).to(immediateExchange()).with("order");
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(deadLetterExchange()).with("order");
    }


    /*******************************************************延时队列***************************************************************/


}
