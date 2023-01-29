package org.example.frank.examples;

import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.examples.adapters.TestAdapter;
import org.example.frank.serverless.ServerlessAdapter;
import org.example.frank.serverless.aws.services.TestAwsAdapter;

import java.io.IOException;

public class App {
    private static TestAwsAdapter adapter;

    public static void main(String[] args) throws ConfigurationException, IOException, PipeRunException, PipeStartException {
        long start = System.currentTimeMillis();

        adapter = new TestAwsAdapter();
        finish(start, "Time Elapsed Before Startup:");

        sendMessage(new Message("Hello world 1"));
        sendMessage(new Message("Hello world 2"));

        finish(start, "Time Elapsed Total:");
    }

    public static void sendMessage(Message request) throws IOException, PipeRunException {
        long start = System.currentTimeMillis();

        PipeLineSession session = new PipeLineSession();
        session.put("name", "John Doe");
        String result = adapter.handleRequest("echo",null);
        System.out.println("Result: " + result);
        finish(start, "Time Elapsed for single message:");
    }

    private static void finish(long start, String message){
        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println(message + " " + timeElapsed);
    }

}
