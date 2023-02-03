package org.example.frank.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import org.example.frank.serverless.ITestAdapter;

public interface IAwsTestAdapter<REQ,REP> extends ITestAdapter {
    public REP handleRequest(REQ request, Context context);
}
