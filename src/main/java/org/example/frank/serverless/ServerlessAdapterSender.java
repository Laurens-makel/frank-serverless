package org.example.frank.serverless;

import lombok.Getter;
import lombok.Setter;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.SenderException;
import nl.nn.adapterframework.core.TimeoutException;
import nl.nn.adapterframework.senders.SenderWithParametersBase;
import nl.nn.adapterframework.stream.Message;

public class ServerlessAdapterSender extends SenderWithParametersBase {
    private @Getter @Setter String adapter;

    @Override
    public Message sendMessage(Message message, PipeLineSession session) throws SenderException, TimeoutException {
        ServerlessAdapter adapter = ServerlessAdapterDispatcher.getAdapter(getAdapter());

        try {
            return adapter.process(String.valueOf(message.getContext().get(PipeLineSession.messageIdKey)), message, session).getResult();
        } catch (PipeRunException e) {
            throw new SenderException("Failed to send message", e);
        }
    }

}
