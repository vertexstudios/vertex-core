package net.threadly.core.service;

import net.threadly.core.command.CommandSpec;

import java.util.Optional;
import java.util.Set;

public interface ICommandService extends IService {
    Optional<CommandSpec> getCommand(String input);
    void register();
    Set<CommandSpec> load(Class<?> clazz);
}
