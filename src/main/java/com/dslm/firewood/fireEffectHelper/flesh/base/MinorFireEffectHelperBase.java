package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;

abstract public class MinorFireEffectHelperBase extends FireEffectHelperBase implements MinorFireEffectHelperInterface
{
    public MinorFireEffectHelperBase(FireEffectNBTDataInterface defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MinorFireEffectHelperBase(String id)
    {
        super(id);
    }
}
