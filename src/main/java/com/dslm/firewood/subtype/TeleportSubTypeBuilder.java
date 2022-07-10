package com.dslm.firewood.subtype;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TeleportSubTypeBuilder extends FireEffectSubTypeBuilder
{
    public static final String DIM_FROM_BLACKLIST = "dim_from_blacklist";
    public static final String DIM_FROM_LIST = "dim_from_list";
    public static final String DIM_TO_BLACKLIST = "dim_to_blacklist";
    public static final String DIM_TO_LIST = "dim_to_list";
    
    @Override
    public TeleportSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return new TeleportSubType((FireEffectSubType) super.getNewData(resourceLocation, jsonObject),
                jsonObject.get(DIM_FROM_BLACKLIST),
                jsonObject.get(DIM_FROM_LIST),
                jsonObject.get(DIM_TO_BLACKLIST),
                jsonObject.get(DIM_TO_LIST));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new TeleportSubType(buf);
    }
}
