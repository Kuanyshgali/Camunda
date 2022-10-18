//package com.logstream.demo;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class SampleController {
//
//    Logger logger = LoggerFactory.getLogger(SampleController.class);
//
//    private final RabbitTemplate template;
//
//    public SampleController(RabbitTemplate template) {
//        this.template = template;
//    }
//
//    @PostMapping("/emit")
//    public ResponseEntity<String> emit(@RequestBody String message){
//        logger.info("EmitTomyQueue");
//        template.setExchange("TestEx");
//        template.convertAndSend("myQueue", message);
//        return ResponseEntity.ok("Success emit to queue");
//    }
//}
