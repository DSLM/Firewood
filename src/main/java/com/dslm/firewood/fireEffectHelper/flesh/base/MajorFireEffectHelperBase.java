package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;

abstract public class MajorFireEffectHelperBase extends FireEffectHelperBase implements MajorFireEffectHelperInterface
{
    public MajorFireEffectHelperBase(FireEffectNBTData defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MajorFireEffectHelperBase(String id)
    {
        super(id);
    }
    
}
