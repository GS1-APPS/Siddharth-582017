package org.gs1us.sgg.gbservice.util;

import java.util.List;

import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.ConflictException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.ValidationException;

public abstract class PutHandler<T>
{
    public Action put(T resource, Action action) throws GlobalBrokerException
    {
        List<ProductValidationError> validationErrors = validate(resource);
        if (validationErrors != null && validationErrors.size() > 0)
            throw new ValidationException(validationErrors);
        else if (action != Action.VALIDATE)
        {
            T oldResource = get(resource);
            if (oldResource == null)
            {
                if (action == Action.UPDATE)
                    throw noSuchResourceException();
                else
                {
                    create(resource);
                    return Action.CREATE;
                }
            }
            else if (!equals(oldResource, resource))
            {
                if (action == Action.CREATE)
                    throw new ConflictException();
                else
                {
                    update(oldResource, resource);
                    return Action.UPDATE;
                }
            }
            else
                return Action.UPDATE;
        }
        else
            return Action.VALIDATE;
    }
    
    protected abstract List<ProductValidationError> validate(T resource);
    
    protected abstract T get(T resource) throws GlobalBrokerException;
    
    protected abstract void create(T resource) throws GlobalBrokerException;
    
    protected abstract void update(T oldResource, T newResource) throws GlobalBrokerException;
    
    protected abstract boolean equals(T oldResource, T newResource);
    
    protected abstract GlobalBrokerException noSuchResourceException();
}
