package com.dslm.firewood.subtype;

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
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.getNewData(resourceLocation, jsonObject);
        if(fireEffectSubType == null)
        {
            return null;
        }
        return new SetBlockNameSubType(fireEffectSubType, jsonObject.get(ORDER), jsonObject.get(CHECK_ORDER));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new SetBlockNameSubType(buf);
    }
}
