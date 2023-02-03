package org.example.frank.serverless;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import nl.nn.adapterframework.configuration.ConfigurationException;
import nl.nn.adapterframework.core.*;
import nl.nn.adapterframework.processors.CorePipeLineProcessor;
import nl.nn.adapterframework.processors.CorePipeProcessor;
import nl.nn.adapterframework.stream.Message;

public abstract class ServerlessAdapter implements INamedObject {
    private @Getter @Setter String name;
    protected PipeLine pipeLine = new PipeLine();

    public ServerlessAdapter(String name) throws ConfigurationException, PipeStartException {
        create(name);
    }

    protected void create(String name) throws ConfigurationException, PipeStartException {
        this.name = name;
        pipeLine.setOwner(this);

        setProcessor();
        createPipeline(pipeLine);
        pipeLine.setPipeLineExits(createExits());
        pipeLine.configure();
        pipeLine.start();

        // Register adapter with Dispatcher so other adapters in the Serverless environment can find it
        ServerlessAdapterDispatcher.addAdapter(this);
    }

    // Build pipeline to your functional requirements
    abstract protected void createPipeline(PipeLine pipeLine) throws ConfigurationException, PipeStartException;

    // Core processors
    protected void setProcessor(){
        CorePipeLineProcessor processor = new CorePipeLineProcessor();
        processor.setPipeProcessor(new CorePipeProcessor());
        pipeLine.setPipeLineProcessor(processor);
    }

    // Could be Overriden if required
    protected PipeLineExits createExits(){
        PipeLineExits exits = new PipeLineExits();

        exits.registerPipeLineExit(createSuccessExit());
        exits.registerPipeLineExit(createErrorExit());

        return exits;
    }
    protected PipeLineExit createSuccessExit(){
        PipeLineExit success = new PipeLineExit();
        success.setName("EXIT");
        success.setState(PipeLine.ExitState.SUCCESS);

        return success;
    }
    protected PipeLineExit createErrorExit(){
        PipeLineExit error = new PipeLineExit();
        error.setName("ERROR");
        error.setState(PipeLine.ExitState.ERROR);

        return error;
    }

    // Main entry point
    public PipeLineResult process(String messageId, Message message, PipeLineSession pipeLineSession) throws PipeRunException {
        return pipeLine.process(messageId, message, pipeLineSession);
    }

    protected <P extends INamedObject> P create(@NotNull String name, @NotNull P object){
        object.setName(name);
        return object;
    }
}
