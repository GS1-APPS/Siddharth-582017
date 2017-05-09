package org.gs1us.sgg.daoutil.jpa;

import org.gs1us.sgg.daoutil.KeyMapper;

public class LongKeyMapper implements KeyMapper<Long>
{

    @Override
    public String keyToId(Long key)
    {
        if (key == 0)
            return null;
        else
            return String.valueOf(key);
    }

    @Override
    public Long idToKey(String id)
    {
        if (id == null)
            return 0L;
        else
        {
            try
            {
                Long key = Long.parseLong(id);
                if (key <= 0)
                    throw new IllegalArgumentException();
                return key;
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException();
            }
        }
    }

}
