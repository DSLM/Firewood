package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;

abstract public class MajorFireEffectHelperBase extends FireEffectHelperBase implements MajorFireEffectHelperInterface
{
    public MajorFireEffectHelperBase(FireEffectNBTDataInterface defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MajorFireEffectHelperBase(String id)
    {
        super(id);
    }
    
}
