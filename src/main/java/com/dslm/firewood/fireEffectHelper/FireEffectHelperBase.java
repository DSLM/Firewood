package com.dslm.firewood.fireEffectHelper;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

abstract public class FireEffectHelperBase implements FireEffectHelperInterface
{
    protected final HashMap<String, String> defaultData;
    protected final String id;
    
    public FireEffectHelperBase(HashMap<String, String> defaultData, String id)
    {
        this.defaultData = defaultData;
        this.id = id;
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(defaultData);
    }
    
    @Override
    public HashMap<String, String> getDefaultData()
    {
        return defaultData;
    }
    
    @Override
    public String getId()
    {
        return id;
    }
    
    @Override
    public String getJEIString(HashMap<String, String> data)
    {
        return data.get("type");
    }
}

