package org.example.frank.serverless.aws.services;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.pipes.EchoPipe;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.serverless.aws.AwsAdapter;

public class TestAwsAdapter extends AwsAdapter<String, String> {

    public TestAwsAdapter() throws ConfigurationException, PipeStartException {
        super("Test");
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
        pipeLine.addPipe(create("echo", new EchoPipe()));
    }

    @Override
    protected Message asMessage(String event, PipeLineSession session) {
        return new Message(event);
    }

    @Override
    protected String extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception {
        return result.getResult().asString();
    }

    @Override
    protected String extractErrorResult(Message request, PipeRunException e, PipeLineSession session) {
        return e.getMessage();
    }
}
