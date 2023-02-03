package org.example.frank.serverless;

import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.pipes.FixedResultPipe;
import nl.nn.adapterframework.stream.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerlessAdapterTest {

    @Test
    public void testEchoPipe() throws Exception {
        ServerlessAdapter adapter = new ServerlessTestAdapter();
        Message message = new Message("Hello World");
        PipeLineResult result = adapter.process("dummy",  message, new PipeLineSession());
        assertEquals(result.getResult().asString(), message.asString());
    }

    @Test
    public void testFixedResultPipe() throws Exception {
        ServerlessTestAdapter adapter = new ServerlessTestAdapter();

        PipeLine pipeLine = new PipeLine();
        FixedResultPipe fixedResultPipe = new FixedResultPipe();
        fixedResultPipe.setName("FXR");
        fixedResultPipe.setFilename("files/xml/hello.txt");
        pipeLine.addPipe(fixedResultPipe);
        adapter.setPipeline(pipeLine);

        Message message = new Message("Hello World");
        PipeLineResult result = adapter.process("dummy",  message, new PipeLineSession());
        assertEquals(result.getResult().asString(), "hello");
    }
}
