package org.example.frank.examples.adapters.pipes;

import nl.nn.adapterframework.pipes.FixedResultPipe;

public class LoadMembersPipe extends FixedResultPipe {

    public LoadMembersPipe(String name, String file){
        setName(name);
        setFilename(file);
    }

}