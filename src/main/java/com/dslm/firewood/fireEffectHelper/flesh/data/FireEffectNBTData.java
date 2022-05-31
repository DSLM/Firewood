package com.dslm.firewood.fireEffectHelper.flesh.data;

import java.util.HashMap;

import static com.dslm.firewood.util.StaticValue.SUB_TYPE;
import static com.dslm.firewood.util.StaticValue.TYPE;

public class FireEffectNBTData extends HashMap<String, String>
{
    public String getType()
    {
        return super.get(TYPE);
    }
    
    public String getSubType()
    {
        return super.get(SUB_TYPE);
    }
    
    public FireEffectNBTData copy()
    {
        FireEffectNBTData newOne = new FireEffectNBTData();
        for(Entry<String, String> entry : this.entrySet())
        {
            newOne.put(entry.getKey(), entry.getValue());
        }
        return newOne;
    }
}
