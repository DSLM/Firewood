package com.dslm.firewood.fireeffecthelper.flesh;

import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;

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
