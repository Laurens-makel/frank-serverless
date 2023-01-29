package org.example.frank.serverless.aws.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLineResult;
import nl.nn.adapterframework.core.PipeLineSession;
import nl.nn.adapterframework.core.PipeRunException;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.stream.Message;
import org.example.frank.serverless.aws.AwsAdapter;

import java.util.Map;


public abstract class ApiGatewayAdapter extends AwsAdapter<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public ApiGatewayAdapter(String name) throws ConfigurationException, PipeStartException {
        super(name);
    }

    @Override
    protected APIGatewayProxyResponseEvent extractResult(Message request, PipeLineResult result, PipeLineSession session) throws Exception {
        return response(request)
                .withStatusCode(result.getExitCode())
                .withBody(result.getResult().asString());
    }

    @Override
    protected APIGatewayProxyResponseEvent extractErrorResult(Message request, PipeRunException e, PipeLineSession session) {
        return response(request)
                .withStatusCode(500)
                .withBody(e.getMessage());
    }

    private APIGatewayProxyResponseEvent response(Message request){
        return (APIGatewayProxyResponseEvent) request.getContext().get(PipeLineSession.HTTP_RESPONSE_KEY);
    }

    protected Message asMessage(APIGatewayProxyRequestEvent event, PipeLineSession session){
        Message request = new Message(event.getBody());
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        Map<String, Object> messageContext = request.getContext();
        messageContext.put(PipeLineSession.HTTP_REQUEST_KEY,  event);
        messageContext.put(PipeLineSession.HTTP_RESPONSE_KEY, response);

        for (Map.Entry<String, String> header : event.getHeaders().entrySet()) {
            messageContext.put("Headers."+header.getKey(), header.getValue());
            session.put(header.getKey(), header.getValue());
        }

        for (Map.Entry<String, String> parameter : event.getPathParameters().entrySet()) {
            messageContext.put("PathParameters."+parameter.getKey(), parameter.getValue());
            session.put(parameter.getKey(), parameter.getValue());
        }

        for (Map.Entry<String, String> parameter : event.getQueryStringParameters().entrySet()) {
            messageContext.put("QueryStringParameters."+parameter.getKey(), parameter.getValue());
            session.put(parameter.getKey(), parameter.getValue());
        }

        for (Map.Entry<String, String> stageVariable : event.getStageVariables().entrySet()) {
            messageContext.put("StageVariables."+stageVariable.getKey(), stageVariable.getValue());
            session.put(stageVariable.getKey(), stageVariable.getValue());
        }

        return request;
    }

}
