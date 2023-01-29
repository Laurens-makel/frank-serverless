package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;


public abstract class AwsAdapter<REQ,REP> extends AwsBaseAdapter<REQ,REP> {

    public AwsAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    public REP handleRequest(REQ request, Context context) {
        PipeLineSession session = createPipelineSession(context);
        Message message = createMessageAndPopulateSession(request, session);

        return (REP) handleMessage(message, session);
    }

    abstract protected Message createMessageAndPopulateSession(REQ request, PipeLineSession session);

}
