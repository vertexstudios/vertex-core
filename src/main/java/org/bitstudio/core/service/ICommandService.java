package org.bitstudio.core.service;

import org.bitstudio.core.conversor.TypeConversor;
import org.bitstudio.core.util.Registry;

public interface ICommandService extends IService {
    void register(Registry<String, TypeConversor<?,?>> conversors);
    void load(Class<?> clazz);
}
