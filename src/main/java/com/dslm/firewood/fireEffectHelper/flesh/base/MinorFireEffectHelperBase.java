package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;

abstract public class MinorFireEffectHelperBase extends FireEffectHelperBase implements MinorFireEffectHelperInterface
{
    public MinorFireEffectHelperBase(FireEffectNBTData defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MinorFireEffectHelperBase(String id)
    {
        super(id);
    }
}
