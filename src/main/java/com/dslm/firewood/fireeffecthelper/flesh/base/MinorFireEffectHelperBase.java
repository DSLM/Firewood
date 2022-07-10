package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;

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
