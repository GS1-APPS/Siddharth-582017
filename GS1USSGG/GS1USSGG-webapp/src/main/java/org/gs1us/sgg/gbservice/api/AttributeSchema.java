package org.gs1us.sgg.gbservice.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gs1us.sgg.util.ChainedIterator;
import org.gs1us.sgg.util.FilterIterator;


public class AttributeSchema implements Iterable<AttributeDesc>
{
    private List<AttributeSchemaModule> m_modules;
    
    public AttributeSchema()
    {
        m_modules = new ArrayList<>();
    }
    
    public void addModule(AttributeSchemaModule module)
    {
        m_modules.add(module);
    }
    
    public Iterator<AttributeDesc> iterator()
    {
        return new ModulesAttributesIterator(m_modules.iterator());
    }
    
    public Iterator<AttributeDesc> selectedAttributesIterator(final HasAttributes o)
    {
        return new ModulesAttributesIterator(new FilterIterator<AttributeSchemaModule>(m_modules.iterator())
            {
                @Override
                protected boolean isIncluded(AttributeSchemaModule module)
                {
                    return module.isSelected(o);
                }
  
            });

    }

    private class ModulesAttributesIterator extends ChainedIterator<AttributeDesc>
    {
        private Iterator<AttributeSchemaModule> m_modulesIterator;
        
        public ModulesAttributesIterator(Iterator<AttributeSchemaModule> modulesIterator)
        {
            super();
            m_modulesIterator = modulesIterator;
        }


        @Override
        protected Iterator<AttributeDesc> nextIterator()
        {
            if (m_modulesIterator.hasNext())
                return m_modulesIterator.next().iterator();
            else
                return null;
        }

    }

}
