package org.example.frank.serverless;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.pipes.EchoPipe;
import nl.nn.adapterframework.stream.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerlessAdapterTest {

    @Test
    public void testEchoPipe() throws Exception {
        ServerlessAdapter adapter = new EchoServerlessAdapter();
        Message message = new Message("Hello World");
        PipeLineResult result = adapter.process("dummy",  message, new PipeLineSession());
        assertEquals(result.getResult().asString(), message.asString());
    }

    class EchoServerlessAdapter extends ServerlessAdapter {

        public EchoServerlessAdapter() throws ConfigurationException, PipeStartException {
            super("Test Serverless Adapter");
        }

        @Override
        protected void createPipeline(PipeLine pipeLine) throws ConfigurationException {
            EchoPipe echoPipe = new EchoPipe();
            echoPipe.setName("echo");

            pipeLine.addPipe(echoPipe);
        }

    }
}
