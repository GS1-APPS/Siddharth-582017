package org.gs1us.fileimport;

public class IdentityValueConditioner<T> implements ValueConditioner<T>
{

    @Override
    public T condition(T original)
    {
        return original;
    }

}
