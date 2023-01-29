package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineResult;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;

public abstract class AwsAdapter<REQ,REP> extends AwsBaseAdapter<REQ,REP> {

    public AwsAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    public REP handleRequest(REQ request, Context context) {
        PipeLineSession session = createPipelineSession(context);
        Message message = asMessage(request, session);

        return handleMessage(message, session);
    }

    abstract protected Message asMessage(REQ event, PipeLineSession session);


    // generic way of calling a pipeline and translating it to AWS specific objects
    protected REP handleMessage(Message request, PipeLineSession session) {
        try {
            PipeLineResult result = process(session.getMessageId(), request, session);
            return extractResult(request, result, session);
        } catch (PipeRunException e) {
            return extractErrorResult(request, e, session);
        } catch (Exception e) {
            throw new RuntimeException("Could not extract result...", e);
        }
    };

    // translate framework results to AWS results
    abstract protected REP extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception;

    // translate framework errors to AWS errors
    abstract protected REP extractErrorResult(Message request, PipeRunException e, PipeLineSession session);
}
