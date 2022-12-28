package net.threadly.core.conversor;

public interface TypeConversor<T, U> {
    public U convert(T type);

}
