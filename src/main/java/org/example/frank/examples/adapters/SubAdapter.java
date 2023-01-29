package org.example.frank.examples.adapters;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import org.example.frank.examples.adapters.pipes.MembersToJsonValidator;
import org.example.frank.serverless.ServerlessAdapter;

public class SubAdapter extends ServerlessAdapter {

    public SubAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
        pipeLine.addPipe(new MembersToJsonValidator());
    }
}
