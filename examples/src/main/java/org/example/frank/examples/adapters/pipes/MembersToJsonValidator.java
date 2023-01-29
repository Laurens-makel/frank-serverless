package org.example.frank.examples.adapters.pipes;

import nl.nn.adapterframework.pipes.Json2XmlValidator;
import nl.nn.adapterframework.stream.document.DocumentFormat;

public class MembersToJsonValidator extends Json2XmlValidator {

    public MembersToJsonValidator(){
        setName("MembersToJsonValidator");
        setOutputFormat(DocumentFormat.JSON);
        setSchema("files/xsd/members.xsd");
        setThrowException(true);
    }

}
