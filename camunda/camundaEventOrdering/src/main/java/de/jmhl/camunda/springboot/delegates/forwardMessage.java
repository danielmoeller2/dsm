package de.jmhl.camunda.springboot.delegates;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.ArrayList;
import java.util.List;


public class forwardMessage implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        // get engine services
        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String targetExecution ="";
        String targetSignal ="";

        targetExecution = (String) execution.getVariableLocal("targetExecutionID");
        targetSignal = (String) execution.getVariableLocal("targetSignal");
        System.out.println("target:" + targetExecution);

        // deliver a signal to a single execution
            runtimeService
                    .createSignalEvent(targetSignal)
                    .executionId(targetExecution)
                    .send();
    }
}
