package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;

public class MobEffectFireEffectHelper extends SubMajorFireEffectHelperBase
{
    
    public MobEffectFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            setType(id);
            setSubType("");
            setProcess(0);
        }}, id, TargetType.LIVING_ENTITY);
    }
}
