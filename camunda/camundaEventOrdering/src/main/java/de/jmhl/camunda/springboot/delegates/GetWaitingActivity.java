package de.jmhl.camunda.springboot.delegates;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;


public class GetWaitingActivity implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        Map<String, String> activityNameByActivityId;
        String executionID ="";
        boolean isFound = false;
        String messageReceiver = "";

        //now i have to use getVariable instead of getVariableLocal. Nobody will understanmd camunda semantics.
        messageReceiver = (String) execution.getVariable("signalName");

        if (messageReceiver == null || messageReceiver == ""){
            System.out.println("No Message receiver for event. Endless loop: ");
            return;
        }
        else {
            System.out.println("Message receiver: " + messageReceiver);
        }

        activityNameByActivityId = getAllActiveActivities(execution);

        for (Map.Entry<String,String> activeActivity : activityNameByActivityId.entrySet()) {
            System.out.println("Key: " + activeActivity.getKey() + " Value: " + activeActivity.getValue());
            if (activeActivity.getValue().equals(messageReceiver) || activeActivity.getKey().equals(messageReceiver)){
                isFound = true;
                execution.setVariableLocal("forward",true);
                execution.setVariable("forward",true);

                executionID = getExecutionForActivity(execution, activeActivity.getKey());

                execution.setVariableLocal("targetExecutionID",executionID);
                execution.setVariable("targetExecutionID",executionID);

                execution.setVariableLocal("targetSignal",activeActivity.getValue());
                execution.setVariable("targetSignal",activeActivity.getValue());
            }
        }

        if (isFound){
            System.out.println("forwarding event");
        }
        else {
            System.out.println("event must wait");
        }
    }


    public String getExecutionForActivity(DelegateExecution execution,  String activityID){
        String targetExecution="";
        String an ="";
        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ActivityInstance[] activeInstances =  runtimeService.getActivityInstance(execution.getProcessInstanceId()).getChildActivityInstances();

        for (ActivityInstance ai : activeInstances) {
            an = ai.getActivityId();
            if (an.equals(activityID)){
                targetExecution= ai.getExecutionIds()[0];
            }
        }
        return targetExecution;
    }


    public Map<String, String> getAllActiveActivities(DelegateExecution execution) {
        // get engine services
        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessInstance processInstance = (ProcessInstance) execution.getProcessInstance();



        HashMap<String, String> activityNameByActivityId = new HashMap<String, String>();

        // get all active activities of the process instance
        List<String> activeActivityIds =
                runtimeService.getActiveActivityIds(processInstance.getId());

        // get bpmn model of the process instance
        BpmnModelInstance bpmnModelInstance =
                repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());

        for (String activeActivityId : activeActivityIds) {
            // get the speaking name of each activity in the diagram
            ModelElementInstance modelElementById =
                    bpmnModelInstance.getModelElementById(activeActivityId);
            String activityName = modelElementById.getAttributeValue("name");
            if (activityName==null){
                activityName="";
            }
            activityNameByActivityId.put(activeActivityId, activityName);
        }

        // map contains now all active activities
        return activityNameByActivityId;
    }


}
