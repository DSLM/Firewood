package com.dslm.firewood.fireEffectHelper.flesh.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.dslm.firewood.util.StaticValue.SUB_TYPE;
import static com.dslm.firewood.util.StaticValue.TYPE;

public class FireEffectNBTData implements FireEffectNBTDataInterface
{
    HashMap<String, String> data = new HashMap<>();
    
    public String getType()
    {
        return data.get(TYPE);
    }
    
    public String getSubType()
    {
        return data.get(SUB_TYPE);
    }
    
    @Override
    public String get(String key)
    {
        return data.get(key);
    }
    
    @Override
    public String put(String key, String value)
    {
        return data.put(key, value);
    }
    
    @Override
    public FireEffectNBTDataInterface copy()
    {
        FireEffectNBTData newOne = new FireEffectNBTData();
        for(Map.Entry<String, String> entry : data.entrySet())
        {
            newOne.put(entry.getKey(), entry.getValue());
        }
        return newOne;
    }
    
    @Override
    public Set<String> keySet()
    {
        return data.keySet();
    }
    
    @Override
    public int size()
    {
        return data.size();
    }
}
