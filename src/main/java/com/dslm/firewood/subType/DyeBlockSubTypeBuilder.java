package com.dslm.firewood.subType;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class DyeBlockSubTypeBuilder extends FireEffectSubTypeBuilder
{
    public static final String COLOR_ORDER = "ColorOrder";
    
    @Override
    public DyeBlockSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return new DyeBlockSubType((FireEffectSubType) super.getNewData(resourceLocation, jsonObject), jsonObject.get(COLOR_ORDER));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new DyeBlockSubType(buf);
    }
}
