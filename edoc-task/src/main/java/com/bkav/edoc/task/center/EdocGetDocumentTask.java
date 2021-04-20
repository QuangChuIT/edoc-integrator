package com.bkav.edoc.task.center;

import org.apache.log4j.Logger;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.task.Task;

public class EdocGetDocumentTask implements Task, ManagedLifecycle {
    private SynapseEnvironment synapseEnvironment;

    /**
     * Execute method will be invoked by the QuartzJOb.
     */
    @Override
    public void execute() {
        LOGGER.info("------------------------ Edoc Get Documents Task Invoke -----------------------");
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

    private final static Logger LOGGER = Logger.getLogger(EdocGetDocumentTask.class);

}
