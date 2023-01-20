package net.threadly.core.service;

import net.threadly.core.conversor.TypeConversor;
import net.threadly.core.util.Registry;

public interface ICommandService extends IService {
    void register(Registry<String, TypeConversor<?,?>> conversors);
    void load(Class<?> clazz);
}
