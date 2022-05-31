package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import net.minecraft.nbt.CompoundTag;

abstract public class FireEffectHelperBase implements FireEffectHelperInterface
{
    protected final FireEffectNBTData DEFAULT_DATA;
    protected final String ID;
    public static final String TYPE = "type";
    
    public FireEffectHelperBase(FireEffectNBTData defaultData, String id)
    {
        this.DEFAULT_DATA = defaultData;
        this.ID = id;
    }
    
    public FireEffectHelperBase(String id)
    {
        this(new FireEffectNBTData()
        {{
            put(TYPE, id);
        }}, id);
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(DEFAULT_DATA);
    }
    
    @Override
    public FireEffectNBTData getDefaultData()
    {
        return DEFAULT_DATA.copy();
    }
    
    @Override
    public String getId()
    {
        return ID;
    }
    
    @Override
    public String getJEIString(FireEffectNBTData data)
    {
        return data.getType();
    }
}

