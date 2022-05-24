package com.dslm.firewood.fireEffectHelper.block.baseClass;


import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class RangedFireEffectDataBuilder
{
    public RangedFireEffectDataBuilder()
    {
    
    }
    
    public RangedFireEffectData getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return new RangedFireEffectData(resourceLocation, jsonObject);
    }
}