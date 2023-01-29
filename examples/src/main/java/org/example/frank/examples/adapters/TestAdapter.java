package org.example.frank.examples.adapters;

import org.example.frank.examples.adapters.pipes.LoadMembersPipe;
import org.example.frank.examples.adapters.pipes.ServerlessAdapterSenderPipe;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import org.example.frank.serverless.ServerlessAdapter;

public class TestAdapter extends ServerlessAdapter {

    public TestAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
        try {
            SubAdapter subAdapter = new SubAdapter("SubAdapter");

            pipeLine.addPipe(new LoadMembersPipe("Load members", "/files/xml/members.xml"));
            pipeLine.addPipe(new ServerlessAdapterSenderPipe("Send members", subAdapter));
        } catch (Exception e){
            throw new ConfigurationException(e);
        }
    }

}
