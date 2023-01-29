package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineResult;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.serverless.ServerlessAdapter;

// REQ = AWS Specific request class, REP = AWS Specific reply class
public abstract class AwsBaseAdapter<REQ,REP> extends ServerlessAdapter implements RequestHandler<REQ,REP> {
    public final String LAMBDA_CLIENT_CONTEXT_KEY       = "LambdaClientContext";
    public final String LAMBDA_FUNCTION_NAME_KEY        = "LambdaFunctionName";
    public final String LAMBDA_FUNCTION_VERSION_KEY     = "LambdaFunctionVersion";
    public final String LAMBDA_IDENTITY_KEY             = "LambdaIdentity";
    public final String LAMBDA_INVOKED_FUNCTION_ARN_KEY = "LambdaInvokedFunctionARN";
    public final String LAMBDA_LOGGER_KEY               = "LambdaLogger";

    public AwsBaseAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    // Reads AWS Lambda Context into Pipeline Session.
    protected PipeLineSession createPipelineSession(Context context){
        PipeLineSession session = new PipeLineSession();

        if(context != null){
            session.put(PipeLineSession.messageIdKey,    context.getAwsRequestId());
            session.put(LAMBDA_CLIENT_CONTEXT_KEY,       context.getClientContext());
            session.put(LAMBDA_FUNCTION_NAME_KEY,        context.getFunctionName());
            session.put(LAMBDA_FUNCTION_VERSION_KEY,     context.getFunctionVersion());
            session.put(LAMBDA_IDENTITY_KEY,             context.getIdentity());
            session.put(LAMBDA_INVOKED_FUNCTION_ARN_KEY, context.getInvokedFunctionArn());
            session.put(LAMBDA_LOGGER_KEY,               context.getLogger());
        }

        return session;
    }

    // generic way of calling a pipeline and translating it to AWS specific objects
    protected Object handleMessage(Message request, PipeLineSession session) {
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
    abstract protected Object extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception;

    // translate framework errors to AWS errors
    abstract protected Object extractErrorResult(Message request, PipeRunException e, PipeLineSession session);
}
