package de.jmhl.camunda.springboot.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.xml.SpinXmlElement;

import static org.camunda.spin.Spin.XML;

public class extractXML implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {

        //extract DMN relevant attributes und transfer them to local context
        //initialize order
        String event = (String) execution.getVariableLocal("locEvent");
        SpinXmlElement xml = XML(event);

        String eventSource =  xml.xPath("/event/source").string();
        String eventName =  xml.xPath("/event/name").string();
        execution.setVariableLocal("eventSource", eventSource);
        execution.setVariableLocal("eventName", eventName);
    }

}