package org.exagon.core.service;

import org.exagon.core.conversor.TypeConversor;
import org.exagon.core.util.Registry;

public interface ICommandService extends IService {
    void register(Registry<String, TypeConversor<?,?>> conversors);
    void load(Class<?> clazz);
}
