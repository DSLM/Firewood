package com.dslm.firewood.fireEffectHelper.flesh.data;

import java.util.HashMap;

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;
import static com.dslm.firewood.fireEffectHelper.flesh.base.TransmuteFireEffectHelperBase.SUB_TAG_ID;

public class FireEffectNBTData extends HashMap<String, String>
{
    public String getType()
    {
        return super.get(TYPE);
    }
    
    public String getSubType()
    {
        return super.get(SUB_TAG_ID);
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
