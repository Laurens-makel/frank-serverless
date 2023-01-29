package org.example.frank.examples.adapters;

import org.example.frank.examples.adapters.pipes.LoadMembersPipe;
import org.example.frank.examples.adapters.pipes.ServerlessAdapterSenderPipe;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import org.example.frank.serverless.ServerlessAdapter;

public class TestAdapter extends ServerlessAdapter {

    private SubAdapter subAdapter = new SubAdapter("SubAdapter");

    public TestAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
        pipeLine.addPipe(new LoadMembersPipe("Load members","/files/xml/members.xml"));
        pipeLine.addPipe(new ServerlessAdapterSenderPipe("Send members", "SubAdapter"));
    }

}
