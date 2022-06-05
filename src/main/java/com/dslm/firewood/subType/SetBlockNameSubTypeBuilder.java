package com.dslm.firewood.subType;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SetBlockNameSubTypeBuilder extends FireEffectSubTypeBuilder
{
    public static final String ORDER = "order";
    public static final String CHECK_ORDER = "checkOrder";
    
    @Override
    public SetBlockNameSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return new SetBlockNameSubType((FireEffectSubType) super.getNewData(resourceLocation, jsonObject), jsonObject.get(ORDER), jsonObject.get(CHECK_ORDER));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new SetBlockNameSubType(buf);
    }
}
