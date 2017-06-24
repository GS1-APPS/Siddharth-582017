package org.gs1us.fileimport;

public interface ValueConditioner<T>
{
    T condition(T original);
}
