package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.PipeStartException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AwsAdapterTest<REQ,REP> {
    protected ObjectMapper objectMapper = new ObjectMapper();

    private final Class<REP> persistentClassReply;
    private final Class<REQ> persistentClassRequest;
    private String eventSource = null;

    public AwsAdapterTest(String eventSource) {
        this.persistentClassReply = (Class<REP>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];

        this.persistentClassRequest = (Class<REQ>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.eventSource = eventSource;
    }

    @Test
    public void testEchoPipe() throws Exception {
        // given
        AwsAdapter<REQ,REP> adapter = getAdapter();

        REQ request = request();
        Context mockedContext = context();

        // when
        REP reply = adapter.handleRequest(request, mockedContext);

        // then
        assertEquals(getRequestBody(request), getReplyBody(reply));
    }

    protected REQ request() throws IOException {
        return objectMapper.readValue(new File("src/test/resources/"+eventSource), persistentClassRequest);
    }

    protected Context context(){
        return Mockito.mock(Context.class);
    }

    abstract protected AwsAdapter<REQ,REP> getAdapter() throws ConfigurationException, PipeStartException;
    abstract protected String getRequestBody(REQ request);
    abstract protected String getReplyBody(REP reply);
}
