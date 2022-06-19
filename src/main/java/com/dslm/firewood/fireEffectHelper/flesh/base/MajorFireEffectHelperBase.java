package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;

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
