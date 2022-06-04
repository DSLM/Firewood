package com.dslm.firewood.subType;


import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FireEffectSubTypeBuilder implements FireEffectSubTypeBuilderBase
{
    public FireEffectSubTypeBuilder()
    {
    
    }
    
    public FireEffectSubTypeBase getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return jsonObject.get(StaticValue.TYPE) == null ||
                jsonObject.get(StaticValue.SUB_TYPE) == null ||
                jsonObject.get(StaticValue.COLOR) == null ||
                jsonObject.get(StaticValue.DAMAGE) == null ||
                jsonObject.get(StaticValue.MIN_HEALTH) == null ||
                jsonObject.get(StaticValue.PROCESS) == null ||
                jsonObject.get(StaticValue.CHANCE) == null ||
                jsonObject.get(StaticValue.RANGE) == null ?
                null :
                new FireEffectSubType(resourceLocation, jsonObject);
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new FireEffectSubType(buf);
    }
}