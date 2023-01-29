package org.example.frank.serverless.aws.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineResult;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.serverless.aws.AwsBatchAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class SqsAdapter extends AwsBatchAdapter<SQSEvent, SQSBatchResponse, SQSEvent.SQSMessage, Void> {
    protected boolean isBatchItemFailureAllowed;
    private final String FAILURES_SESSION_KEY = "BatchItemFailures";

    public SqsAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected void populateBatchSession(PipeLineSession session) {
        if(isBatchItemFailureAllowed){
            session.put(FAILURES_SESSION_KEY, new ArrayList<SQSBatchResponse.BatchItemFailure>());
        }
    }

    @Override
    protected Iterable<SQSEvent.SQSMessage> asIterable(SQSEvent event) {
        return event.getRecords();
    }

    @Override
    protected void populateBatchItemSession(Context context, PipeLineSession session, SQSEvent.SQSMessage message) {
        session.put(PipeLineSession.messageIdKey, message.getMessageId());
    }

    @Override
    protected Message asMessage(SQSEvent.SQSMessage batchItem, PipeLineSession session) {
        return new Message(batchItem.getBody());
    }

    @Override
    protected Void handleMessage(Message request, PipeLineSession session) {
        try {
            process(session.getMessageId(), request, session);
        } catch (PipeRunException e) {
            if(isBatchItemFailureAllowed){
                storeFailureInSession(session);
            } else {
                throw new RuntimeException("BatchItem failures are not allowed, aborting batch.");
            }
        }
        return null;
    }

    @Override
    protected SQSBatchResponse getBatchResult(PipeLineSession batchSession) {
        SQSBatchResponse response = new SQSBatchResponse();
        if(isBatchItemFailureAllowed){
            List<SQSBatchResponse.BatchItemFailure> failures = getBatchItemFailures(batchSession);
            response.setBatchItemFailures(failures);
        }

        return response;
    }

    protected void storeFailureInSession(PipeLineSession session){
        SQSBatchResponse.BatchItemFailure failure = new SQSBatchResponse.BatchItemFailure(session.getMessageId());
        PipeLineSession batchSession = getBatchSession(session);
        getBatchItemFailures(batchSession).add(failure);
    }

    protected List<SQSBatchResponse.BatchItemFailure> getBatchItemFailures(PipeLineSession batchSession){
        return (List<SQSBatchResponse.BatchItemFailure>) batchSession.get(FAILURES_SESSION_KEY);
    }

    @Override
    protected Void extractResult(Message request, PipeLineResult result, PipeLineSession session) {
        return null;
    }

    @Override
    protected Void extractErrorResult(Message request, PipeRunException e, PipeLineSession session) {
        return null;
    }
}
