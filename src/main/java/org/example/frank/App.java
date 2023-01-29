package org.example.frank;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.examples.adapters.TestAdapter;
import org.example.frank.serverless.ServerlessAdapter;

import java.io.IOException;

public class App {
    private static ServerlessAdapter adapter;

    public static void main(String[] args) throws ConfigurationException, IOException, PipeRunException, PipeStartException {
        long start = System.currentTimeMillis();

        adapter = new TestAdapter("Test run");
        finish(start, "Time Elapsed Before Startup:");

        sendMessage(new Message("Hello world 1"));
        sendMessage(new Message("Hello world 2"));

        finish(start, "Time Elapsed Total:");
    }

    public static void sendMessage(Message request) throws IOException, PipeRunException {
        long start = System.currentTimeMillis();

        PipeLineSession session = new PipeLineSession();
        session.put("name", "John Doe");
        PipeLineResult result = adapter.process("",request, session);
        System.out.println("Result: " + result.getResult().asString());
        finish(start, "Time Elapsed for single message:");
    }

    private static void finish(long start, String message){
        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println(message + " " + timeElapsed);
    }

}
