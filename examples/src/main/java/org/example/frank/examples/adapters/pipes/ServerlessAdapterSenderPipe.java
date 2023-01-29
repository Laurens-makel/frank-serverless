package org.example.frank.examples.adapters.pipes;

import nl.nn.adapterframework.pipes.SenderPipe;
import org.example.frank.serverless.ServerlessAdapter;
import org.example.frank.serverless.ServerlessAdapterSender;

public class ServerlessAdapterSenderPipe extends SenderPipe {

    public ServerlessAdapterSenderPipe(String name, ServerlessAdapter adapter){
        setName(name);
        ServerlessAdapterSender serverlessAdapterSender = new ServerlessAdapterSender();
        serverlessAdapterSender.setAdapter(adapter.getName());
        setSender(serverlessAdapterSender);
    }

}
