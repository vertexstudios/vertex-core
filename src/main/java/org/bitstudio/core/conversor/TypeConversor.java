package org.bitstudio.core.conversor;

public interface TypeConversor<T, U> {
    public U convert(T type);

}
