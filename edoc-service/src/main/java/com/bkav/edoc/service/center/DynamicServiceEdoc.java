package com.bkav.edoc.service.center;

import org.apache.log4j.Logger;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class DynamicServiceEdoc extends AbstractMediator implements ManagedLifecycle {
    @Override
    public void init(SynapseEnvironment se) {
        LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!! Dynamic Service Edoc 2.0 Mediator Init !!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean mediate(MessageContext synCtx) {
        LOGGER.info("--------------- eDoc mediator invoker by class mediator ---------------");

        org.apache.axis2.context.MessageContext inMessageContext = ((Axis2MessageContext) synCtx).getAxis2MessageContext();

        SynapseLog synLog = getLog(synCtx);

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("Start : Log mediator");

            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + synCtx.getEnvelope());
            }
        }
        return true;
    }

    private static final Logger LOGGER = Logger.getLogger(DynamicServiceEdoc.class);
}
