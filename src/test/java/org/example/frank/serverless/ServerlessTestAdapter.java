package org.example.frank.serverless;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.pipes.EchoPipe;
import org.example.frank.serverless.ServerlessAdapter;

public class ServerlessTestAdapter extends ServerlessAdapter {

    public ServerlessTestAdapter() throws ConfigurationException, PipeStartException {
        super("Test Adapter");
    }

    public void setPipeline(PipeLine pipeLine) throws ConfigurationException, PipeStartException {
        this.pipeLine = pipeLine;
        create(getName());
    }

    @Override
    protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
        EchoPipe echoPipe = new EchoPipe();
        echoPipe.setName("echo");

        pipeLine.addPipe(echoPipe);
    }
}
