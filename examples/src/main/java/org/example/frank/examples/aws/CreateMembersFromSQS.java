package org.example.frank.examples.aws;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import org.example.frank.serverless.aws.services.SqsAdapter;

public class CreateMembersFromSQS extends SqsAdapter {

    public CreateMembersFromSQS() throws ConfigurationException, PipeStartException {
        super("CreateMembersFromSQS");
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {

    }

}