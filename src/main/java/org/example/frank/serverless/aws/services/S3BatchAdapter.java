package org.example.frank.serverless.aws.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3BatchEvent;
import com.amazonaws.services.lambda.runtime.events.S3BatchResponse;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.stream.Message;
import nl.nn.adapterframework.util.XmlBuilder;
import org.example.frank.serverless.aws.AwsBatchAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class S3BatchAdapter extends AwsBatchAdapter<S3BatchEvent, S3BatchResponse, S3BatchEvent.Task, S3BatchResponse.Result> {
    public final String TASK_ID_KEY = "taskId";
    public final String TASKS_RESULTS_KEY = "TasksResults";

    public S3BatchAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected void populateBatchSession(PipeLineSession batchSession) {
        batchSession.put(TASKS_RESULTS_KEY, new ArrayList<S3BatchResponse.Result>());
    }

    @Override
    protected Iterable<S3BatchEvent.Task> asIterable(S3BatchEvent batch) {
        return batch.getTasks();
    }

    @Override
    protected void populateBatchItemSession(Context context, PipeLineSession session, S3BatchEvent.Task batchItem) {
        session.put(TASK_ID_KEY,   batchItem.getTaskId());
        session.put("s3BucketArn", batchItem.getS3BucketArn());
        session.put("s3Key",       batchItem.getS3Key());
        session.put("s3VersionId", batchItem.getS3VersionId());
    }

    @Override
    protected Message asMessage(S3BatchEvent.Task batchItem, PipeLineSession session) {
        XmlBuilder task = new XmlBuilder("task");
        task.addSubElement("taskId",      batchItem.getTaskId());
        task.addSubElement("s3BucketArn", batchItem.getS3BucketArn());
        task.addSubElement("s3Key",       batchItem.getS3Key());
        task.addSubElement("s3VersionId", batchItem.getS3VersionId());

        return new Message(task.toXML());
    }

    @Override
    protected S3BatchResponse getBatchResult(PipeLineSession batchSession) {
        S3BatchResponse response = new S3BatchResponse();
        response.setResults(getResultsList(batchSession));
        return response;
    }

    @Override
    protected S3BatchResponse.Result extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception {
        S3BatchResponse.Result s3Result = new S3BatchResponse.Result();

        if(result.isSuccessful()){
            s3Result.setResultCode(S3BatchResponse.ResultCode.Succeeded);
        } else {
            s3Result.setResultCode(S3BatchResponse.ResultCode.PermanentFailure);
        }
        s3Result.setResultString(result.getResult().asString());
        s3Result.setTaskId((String) session.get(TASK_ID_KEY));

        List<S3BatchResponse.Result> results = getResultsList(getBatchSession(session));
        results.add(s3Result);

        return s3Result;
    }

    @Override
    protected S3BatchResponse.Result extractErrorResult(Message request, PipeRunException e, PipeLineSession session) {
        S3BatchResponse.Result s3Result = new S3BatchResponse.Result();
        s3Result.setResultCode(S3BatchResponse.ResultCode.TemporaryFailure);
        s3Result.setResultString(e.getMessage());
        s3Result.setTaskId((String) session.get(TASK_ID_KEY));
        return s3Result;
    }

    private List<S3BatchResponse.Result> getResultsList(PipeLineSession batchSession){
        return (List<S3BatchResponse.Result>) batchSession.get(TASKS_RESULTS_KEY);
    }

}
