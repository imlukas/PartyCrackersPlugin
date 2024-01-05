package dev.imlukas.partycrackersplugin.util.command.command.impl;

import dev.imlukas.partycrackersplugin.util.command.language.CompiledObjective;
import dev.imlukas.partycrackersplugin.util.command.language.data.ObjectiveMetadata;

public abstract class ExecutionContext extends CompiledObjective { // Just a friendly rename

    public ExecutionContext(ObjectiveMetadata metadata) {
        super(metadata);
    }
}
