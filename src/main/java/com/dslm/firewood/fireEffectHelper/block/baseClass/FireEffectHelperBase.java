package com.dslm.firewood.fireEffectHelper.block.baseClass;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

abstract public class FireEffectHelperBase implements FireEffectHelperInterface
{
    protected final HashMap<String, String> DEFAULT_DATA;
    protected final String ID;
    
    public FireEffectHelperBase(HashMap<String, String> defaultData, String id)
    {
        this.DEFAULT_DATA = defaultData;
        this.ID = id;
    }
    
    public FireEffectHelperBase(String id)
    {
        this(new HashMap<>()
        {{
            put("type", id);
        }}, id);
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(DEFAULT_DATA);
    }
    
    @Override
    public HashMap<String, String> getDefaultData()
    {
        return DEFAULT_DATA;
    }
    
    @Override
    public String getId()
    {
        return ID;
    }
    
    @Override
    public String getJEIString(HashMap<String, String> data)
    {
        return data.get("type");
    }
}

