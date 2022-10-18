package com.logstream.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.glassfish.jersey.client.internal.HttpUrlConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TestS implements JavaDelegate {

    Logger logger = LoggerFactory.getLogger(TestS.class);
    //
    private final RabbitTemplate template;

    public TestS(RabbitTemplate template) {
        this.template = template;
    }

    //    @PostMapping("/emit")
//    public ResponseEntity<String> emit(@RequestBody String message){
//
//        return ResponseEntity.ok("Success emit to queue");
//    }
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        logger.info("EmitTomyQueue");
        template.setExchange("TestEx");
        String message = (String) delegateExecution.getVariable("text");

        template.convertAndSend("test", message);

    }
}
