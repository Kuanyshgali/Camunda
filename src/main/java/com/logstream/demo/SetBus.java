package com.logstream.demo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SetBus implements JavaDelegate {

    Logger logger = LoggerFactory.getLogger(TestS.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setProcessBusinessKey(String.valueOf(UUID.randomUUID()));
        //delegateExecution.setVariable("id", delegateExecution.getProcessDefinitionId());
        logger.info(delegateExecution.getProcessBusinessKey());
    }
}
