package com.dslm.firewood.fireEffectHelper.flesh.data;

import java.util.HashMap;

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;

public class FireEffectNBTData extends HashMap<String, String>
{
    public String getType()
    {
        return super.get(TYPE);
    }
}
