package com.dslm.firewood.subtype;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MobEffectSubTypeBuilder extends FireEffectSubTypeBuilder
{
    public static final String EFFECTS = "effects";
    public static final String EFFECT_TO_ENEMY = "effects_to_enemy";
    
    @Override
    public MobEffectSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.getNewData(resourceLocation, jsonObject);
        if(fireEffectSubType == null)
        {
            return null;
        }
        return new MobEffectSubType(fireEffectSubType,
                jsonObject.get(EFFECTS),
                jsonObject.get(EFFECT_TO_ENEMY));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new MobEffectSubType(buf);
    }
}
