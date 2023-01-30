package org.example.frank.serverless.aws.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeLine;
import nl.nn.adapterframework.core.PipeStartException;
import nl.nn.adapterframework.pipes.EchoPipe;
import org.example.frank.serverless.aws.AwsAdapter;
import org.example.frank.serverless.aws.AwsAdapterTest;

public class ApiGatewayAdapterTest extends AwsAdapterTest<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public ApiGatewayAdapterTest() {
        super("APIGatewayProxyRequestEvent.json");
    }

    @Override
    protected AwsAdapter<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> getAdapter() throws ConfigurationException, PipeStartException {
        return new EchoApiGatewayAdapter();
    }

    @Override
    protected String getRequestBody(APIGatewayProxyRequestEvent request) {
        return request.getBody();
    }

    @Override
    protected String getReplyBody(APIGatewayProxyResponseEvent reply) {
        return reply.getBody();
    }

    class EchoApiGatewayAdapter extends ApiGatewayAdapter {

        public EchoApiGatewayAdapter() throws ConfigurationException, PipeStartException {
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
