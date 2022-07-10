package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import net.minecraft.nbt.CompoundTag;

abstract public class FireEffectHelperBase implements FireEffectHelperInterface
{
    protected final FireEffectNBTDataInterface DEFAULT_DATA;
    protected final String ID;
    
    public FireEffectHelperBase(FireEffectNBTDataInterface defaultData, String id)
    {
        this.DEFAULT_DATA = defaultData;
        this.ID = id;
    }
    
    public FireEffectHelperBase(String id)
    {
        this(new FireEffectNBTData()
        {{
            setType(id);
        }}, id);
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(DEFAULT_DATA);
    }
    
    @Override
    public FireEffectNBTDataInterface getDefaultData()
    {
        return DEFAULT_DATA.copy();
    }
    
    @Override
    public String getId()
    {
        return ID;
    }
    
    @Override
    public String getJEIString(FireEffectNBTDataInterface data)
    {
        return data.getType();
    }
}

