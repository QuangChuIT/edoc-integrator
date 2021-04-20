package com.bkav.edoc.task.center;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import org.apache.log4j.Logger;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.task.Task;

import java.util.Date;

public class EdocGetDocumentTask implements Task, ManagedLifecycle {
    private SynapseEnvironment synapseEnvironment;
    private String turnOn;

    /**
     * Execute method will be invoked by the QuartzJOb.
     */
    @Override
    public void execute() {
        boolean isTurnOn = GetterUtil.getBoolean(this.getTurnOn(), false);
        LOGGER.info("------------------------ Edoc Get Documents Task Invoke -----------------------");
        if (isTurnOn) {
            LOGGER.info("Start get documents from vpcp at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));

        } else {
            LOGGER.info("------------------------ Get Documents from VPCP turn off -----------------------");
        }

    }

    /**
     * This method should implement the initialization of the
     * implemented parts of the configuration.
     *
     * @param se SynapseEnvironment to be used for initialization
     */
    @Override
    public void init(SynapseEnvironment se) {
        this.synapseEnvironment = se;
    }

    /**
     * This method should implement the destroying of the
     * implemented parts of the configuration.
     */
    @Override
    public void destroy() {

    }

    public SynapseEnvironment getSynapseEnvironment() {
        return synapseEnvironment;
    }

    public void setSynapseEnvironment(SynapseEnvironment synapseEnvironment) {
        this.synapseEnvironment = synapseEnvironment;
    }

    public String getTurnOn() {
        return turnOn;
    }

    public void setTurnOn(String turnOn) {
        this.turnOn = turnOn;
    }

    private final static Logger LOGGER = Logger.getLogger(EdocGetDocumentTask.class);

}
