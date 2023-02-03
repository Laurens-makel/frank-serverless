package org.example.frank.serverless;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;

public interface ITestAdapter {
    public void setPipeline(PipeLine pipeLine) throws ConfigurationException, PipeStartException;
}
