package com.logstream.demo;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@EnableRabbit
@Component
public class Receiver implements JavaDelegate {
    @Autowired
    private ProcessEngine camunda;

    private String bKey;
    private String text;

    public Receiver() {
    }

    public Receiver(ProcessEngine camunda) {
        this.camunda = camunda;
    }

    Logger logger = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        bKey = delegateExecution.getProcessBusinessKey();
        text = (String) delegateExecution.getVariable("text");
    }

    @RabbitHandler
    @RabbitListener(queues = "myQueue", containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    public void listen(String message) {
        logger.info("Recieved from my queue: {}", message);
        handleGoodsShippedEvent(message);
        latch.countDown();
    }

    public void handleGoodsShippedEvent(String message) {
        if (!Objects.equals(message, text))
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            logger.info("All is OK");
        camunda.getRuntimeService().createMessageCorrelation("Message_GoodsShipped")
                .processInstanceBusinessKey(bKey)
                .setVariable("message", message) //
                .correlate();
    }

    public CountDownLatch getLatch() {
        return latch;
    }


}
//**************************************************************************************//
//package com.logstream.demo;
//
////import com.rabbitmq.client.Channel;
////import com.rabbitmq.client.Connection;
////import com.rabbitmq.client.ConnectionFactory;
////import com.rabbitmq.client.DeliverCallback;
//import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
//import org.camunda.bpm.client.task.ExternalTask;
//import org.camunda.bpm.client.task.ExternalTaskHandler;
//import org.camunda.bpm.client.task.ExternalTaskService;
//import org.camunda.bpm.engine.variable.VariableMap;
//import org.camunda.bpm.engine.variable.Variables;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CountDownLatch;
//
////import java.io.IOException;
////import java.nio.charset.StandardCharsets;
////import java.util.concurrent.TimeoutException;
//
//@Component

////@EnableRabbit
//@ExternalTaskSubscription("TestR")
//public class Receiver implements ExternalTaskHandler {
//
//    private VariableMap variables = Variables.createVariables();
//    Logger logger = LoggerFactory.getLogger(Receiver.class);
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    public void listen(String message){
//        logger.info("Recieved from my queue: {}", message);
//        latch.countDown();
//        variables.put("message", message);
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }
//
//    @Override
//    public void execute(ExternalTask externalTask, @NotNull ExternalTaskService externalTaskService) {
//        logger.info("Massage has been send to camunda" + variables);
//        externalTaskService.complete(externalTask, variables);
//    }
//}
//***********************************************************************************************************//
////        ConnectionFactory factory = new ConnectionFactory();
////        factory.setHost("localhost");
////        Connection connection = null;
////        try {
////            connection = factory.newConnection();
////        } catch (IOException | TimeoutException e) {
////            e.printStackTrace();
////        }
////        Channel channel = null;
////        try {
////            assert connection != null;
////            channel = connection.createChannel();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
//////        VariableMap variables = Variables.createVariables();
////        //String a;
////        Channel finalChannel = channel;
////        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
////
////            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
////
////            System.out.println(" [x] Received '" + message + "'");
////            try {
////                a = message;
////                variables.put("result", a);
////                //variables.put("result", message);
////                //externalTaskService.setVariables(externalTask, variables);
////                //externalTaskService.complete(externalTask, variables);
////                System.out.println("The External Task " + externalTask.getId() + " has been completed!" + " With message = " + a);
////            } finally {
////                System.out.println(" [x] Done");
////                assert finalChannel != null;
////                finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////            }
////        };
////
////        try {
////            assert channel != null;
////            channel.basicConsume("myQueue", false, deliverCallback, consumerTag -> {
////            });
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////externalTaskService.complete(externalTask, variables);
////        variables.put("result", a);
//
//
////@EnableRabbit
////@Component
////public class TestR implements JavaDelegate {
////    Logger logger = LoggerFactory.getLogger(TestR.class);
//////    ApplicationContext context = new ClassPathXmlApplicationContext("service-context.xml");
//////    ConnectionFactory connectionFactory = (ConnectionFactory) context.getBean("ConnectionFactory");
////
////
////    @RabbitListener(queues = "myQueue")
////    public void listen(String message){
////        logger.info("Recieved from my queue: {}", message);
////    }
////
////
////    @Override
////    public void execute(DelegateExecution delegateExecution) throws Exception {
////          var a = new Variables();
////
//////        ConnectionFactory factory = new ConnectionFactory();
//////        factory.setHost("localhost");
//////        Connection connection = factory.newConnection();
//////        Channel channel = connection.createChannel();
//////        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
////
////        //System.out.println(messageListenerContainer.);
////
////
//////        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//////            String message = new String(delivery.getBody(), "UTF-8");
//////
//////            System.out.println(" [x] Received '" + message + "'");
//////            try {
//////                delegateExecution.setVariable("message", new ArrayList<String>().add(message));
//////            } finally {
//////                System.out.println(" [x] Done");
//////                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//////            }
//////        };
//////        channel.basicConsume("myQueue", false, deliverCallback, consumerTag -> {});
////
////    }
//
//
//
//
