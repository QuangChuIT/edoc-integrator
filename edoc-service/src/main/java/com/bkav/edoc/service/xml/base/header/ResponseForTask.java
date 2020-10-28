package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResponseForTask extends BaseElement {
    private List<String> taskCodes = new ArrayList<>();

    public List<String> getTaskCodes() {
        return this.taskCodes;
    }

    public ResponseForTask() {
    }

    public ResponseForTask(List<String> taskCodes) {
        this.taskCodes = taskCodes;
    }

    public void setTaskCodes(List<String> taskCodes) {
        this.taskCodes = taskCodes;
    }

    public ResponseForTask addTaskCode(String task) {
        this.taskCodes.add(task);
        return this;
    }

    public static ResponseForTask fromContent(Element element) {
        ResponseForTask responseForTask = new ResponseForTask();
        List<Element> childrenElement = element.getChildren();
        if (childrenElement != null && childrenElement.size() != 0) {
            for (Element children : childrenElement) {
                if ("TaskCode".equals(children.getName())) {
                    responseForTask.addTaskCode(children.getTextTrim());
                }
            }
            return responseForTask;
        } else {
            return null;
        }
    }

    public void accumulate(Element element) {
        Element responseForTask = this.accumulate(element, "ResponseForTask");
        if (this.taskCodes.size() > 0) {
            for (String str : this.taskCodes) {
                this.accumulate(responseForTask, "TaskCode", str);
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("taskCodes", this.taskCodes).toString();
    }
}
