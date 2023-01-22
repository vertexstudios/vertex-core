package org.exagon.core.conversor;

public interface TypeConversor<T, U> {
    public U convert(T type);

}
