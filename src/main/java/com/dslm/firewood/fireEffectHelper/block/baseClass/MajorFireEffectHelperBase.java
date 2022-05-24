package com.dslm.firewood.fireEffectHelper.block.baseClass;

import java.util.HashMap;

abstract public class MajorFireEffectHelperBase extends FireEffectHelperBase implements MajorFireEffectHelperInterface
{
    public MajorFireEffectHelperBase(HashMap<String, String> defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MajorFireEffectHelperBase(String id)
    {
        super(id);
    }
    
}
