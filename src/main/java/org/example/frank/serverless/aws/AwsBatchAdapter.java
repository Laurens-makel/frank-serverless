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

        for(MSG request : asIterable(batch)){
            PipeLineSession session = createPipelineSession(context);
            session.put(BATCH_SESSION_KEY, batchSession);
            populateBatchItemSession(context, session, request);

            Message message = asMessage(request, session);
            handleMessage(message, session);
        }

        return getBatchResult(batchSession);
    }

    // generic way of calling a pipeline and translating it to AWS specific objects
    protected MSGREP handleMessage(Message request, PipeLineSession session) {
        try {
            PipeLineResult result = process(session.getMessageId(), request, session);
            return extractResult(request, result, session);
        } catch (PipeRunException e) {
            return extractErrorResult(request, e, session);
        } catch (Exception e) {
            throw new RuntimeException("Could not extract result...", e);
        }
    };

    // translate framework results to AWS results
    abstract protected MSGREP extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception;

    // translate framework errors to AWS errors
    abstract protected MSGREP extractErrorResult(Message request, PipeRunException e, PipeLineSession session);

    // prepare batch session
    abstract protected void populateBatchSession(PipeLineSession batchSession);

    // start batch processing
    abstract protected Iterable<MSG> asIterable(REQ batch);

    // prepare batch item session, for each item in batch
    abstract protected void populateBatchItemSession(Context context, PipeLineSession session, MSG batchItem);

    // transform raw batch item msg to Message class
    abstract protected Message asMessage(MSG batchItem, PipeLineSession session);

    // read batch results from essions
    abstract protected REP getBatchResult(PipeLineSession batchSession);

    protected PipeLineSession getBatchSession(PipeLineSession session){
        return (PipeLineSession) session.get(BATCH_SESSION_KEY);
    }

}
