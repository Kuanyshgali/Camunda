package com.logstream.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Configuration
public class RabbitConfiguration {

    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    Queue myQueue() {
        return new Queue("myQueue");
    }
    @Bean
    Queue ErrorBatches() {
        return new Queue("ErrorBatches");
    }
    @Bean
    Queue SuccessExpord() {
        return new Queue("SuccessExpord");
    }
    @Bean
    Queue ReadyExport() {
        return new Queue("ReadyExport");
    }

    @Bean
    DirectExchange DirectExchange() {
        return new DirectExchange("TestEx");
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(myQueue()).to(DirectExchange()).with("test");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //container.setQueueNames("myQueue");;
        return factory;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "listen");
    }
}


//    @Bean
//    public MessageConverter messageConverter() {
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        jackson2JsonMessageConverter.setClassMapper(new ClassMapper() {
//
//            @Override
//            public Class<?> toClass(MessageProperties properties) {
//                return Foo.class;
//            }
//
//            @Override
//            public void fromClass(Class<?> clazz, MessageProperties properties) {
//
//            }
//
//        });
//        return jackson2JsonMessageConverter;
//    }


//    @Bean
//    MessageListenerAdapter listenerAdapter(Worker worker) {
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(worker, "processMessage");
//        messageListenerAdapter.setMessageConverter(messageConverter());
//        return messageListenerAdapter;
//    }

//    @Bean
//    public Worker worker() {
//        return new Worker();
//    }
//
//    public static class Worker {
//
//        private final CountDownLatch latch = new CountDownLatch(1);
//
//        public void processMessage(Map<String, String> message) {
//            //System.out.println(message.get(0));
//            //System.out.println(foo);
//            this.latch.countDown();
//        }
//
//    }

//    public static class Foo {
//
//        private String bar = "bar";
//
//        public String getBar() {
//            return this.bar;
//        }
//
//        public void setBar(String bar) {
//            this.bar = bar;
//        }
//
//        @Override
//        public String toString() {
//            return "Foo [bar=" + this.bar + "]";
//        }
//
//    }


//    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);
//
//    @Bean
//    public ConnectionFactory connectionFactory(){ return new CachingConnectionFactory("localhost");}
//
//    @Bean
//    public AmqpAdmin amqpAdmin(){ return new RabbitAdmin(connectionFactory());}
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(){return new RabbitTemplate(connectionFactory());}
//
//    @Bean
//    public Queue myQueue(){return new Queue("myQueue");}
//
//    @Bean
//    FanoutExchange fanoutExchange(){return new FanoutExchange("TestEx");}
//
//    @Bean
//    Binding binding(){return BindingBuilder.bind(myQueue()).to(fanoutExchange());}
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Worker worker) {
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(worker, "processMessage");
//        messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
//        return messageListenerAdapter;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(){
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames("myQueue");
//        container.setMessageListener(listenerAdapter);
//        container.setMessagePropertiesConverter(messageConverter);
//        return container;
//    }
//}
