package org.vertex.core.service;

import org.vertex.core.conversor.TypeConversor;
import org.vertex.core.util.Registry;

public interface ICommandService extends IService {
    void register(Registry<String, TypeConversor<?,?>> conversors);
    void load(Class<?> clazz);
}
