package de.jmhl.camunda.springboot.listeners;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubprocessStart implements ExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(SubprocessStart.class);

    public void notify(DelegateExecution execution) throws Exception {
        logger.info("ProcessStart-Listener Fired: ");
        execution.setVariableLocal("locEvent", execution.getVariable("event"));
        execution.setVariableLocal("forward", false);
        execution.setVariableLocal("signalName", "");
        logger.info("ProcessStart-Listener finished: ");
    }
}