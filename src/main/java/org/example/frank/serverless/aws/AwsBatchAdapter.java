package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineResult;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;

public abstract class AwsBatchAdapter<REQ,REP,MSG,MSGREP> extends AwsBaseAdapter<REQ,REP> {
    public static final String BATCH_SESSION_KEY = "BATCH_SESSION";

    public AwsBatchAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    public REP handleRequest(REQ batch, Context context) {
        PipeLineSession batchSession = createPipelineSession(context);
        populateBatchSession(batchSession);

        for(MSG batchItem : asIterable(batch)){
            PipeLineSession session = createPipelineSession(context);
            session.put(BATCH_SESSION_KEY, batchSession);

            Message message = createMessageAndPopulateBatchItemSession(context, batchItem, session);
            handleMessage(message, session);
        }

        return getBatchResult(batchSession);
    }

    // prepare batch session
    abstract protected void populateBatchSession(PipeLineSession batchSession);

    // start batch processing
    abstract protected Iterable<MSG> asIterable(REQ batch);

    // prepare batch item session, for each item in batch
    // transform raw batch item msg to Message class
    abstract protected Message createMessageAndPopulateBatchItemSession(Context context, MSG batchItem, PipeLineSession session);

    // read batch results from essions
    abstract protected REP getBatchResult(PipeLineSession batchSession);

    protected PipeLineSession getBatchSession(PipeLineSession session){
        return (PipeLineSession) session.get(BATCH_SESSION_KEY);
    }

}
