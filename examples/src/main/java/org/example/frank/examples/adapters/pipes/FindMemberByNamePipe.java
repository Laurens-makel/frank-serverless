package org.example.frank.examples.adapters.pipes;

import nl.nn.adapterframework.parameters.Parameter;
import nl.nn.adapterframework.pipes.XsltPipe;

public class FindMemberByNamePipe extends XsltPipe {

    public FindMemberByNamePipe(){
        this(null);
    }

    public FindMemberByNamePipe(String memberName){
        setName("Retrieve fixed result");
        setXpathExpression("//members/member[@name=$memberName]/@name");

        Parameter parameter = new Parameter();
        parameter.setName("memberName");
        if(memberName == null){
            parameter.setSessionKey("memberName");
        } else {
            parameter.setValue(memberName);
        }
        addParameter(parameter);
    }
}
