package org.example.frank.examples.aws;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import org.example.frank.examples.adapters.SubAdapter;
import org.example.frank.examples.adapters.pipes.LoadMembersPipe;
import org.example.frank.examples.adapters.pipes.ServerlessAdapterSenderPipe;
import org.example.frank.serverless.aws.services.ApiGatewayAdapter;

public class GetMembers extends ApiGatewayAdapter {
    private SubAdapter subAdapter = new SubAdapter("SubAdapter");

    public GetMembers() throws ConfigurationException, PipeStartException {
        super("GetMembers");
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException, PipeStartException {
        SubAdapter subAdapter = new SubAdapter("SubAdapter");

        pipeLine.addPipe(new LoadMembersPipe("Load members", "/files/xml/members.xml"));
        pipeLine.addPipe(new ServerlessAdapterSenderPipe("Send members", subAdapter));
    }

}
