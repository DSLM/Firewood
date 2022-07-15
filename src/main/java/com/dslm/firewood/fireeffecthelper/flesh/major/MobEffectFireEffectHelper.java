package com.dslm.firewood.fireeffecthelper.flesh.major;

import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;

public class MobEffectFireEffectHelper extends SubMajorFireEffectHelperBase
{// TODO: 2022/7/15 自定义效果
    
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
