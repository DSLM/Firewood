package com.dslm.firewood.fireEffectHelper.block.baseClass;

import java.util.HashMap;

abstract public class MinorFireEffectHelperBase extends FireEffectHelperBase implements MinorFireEffectHelperInterface
{
    public MinorFireEffectHelperBase(HashMap<String, String> defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public MinorFireEffectHelperBase(String id)
    {
        super(id);
    }
}
