package com.dslm.firewood.subtype;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PotionSubTypeBuilder extends FireEffectSubTypeBuilder
{
    public static final String BLACKLIST = "blacklist";
    public static final String LIST = "list";
    public static final String COLOR_MIXED = "color_mixed";
    public static final String EFFECT_MULTI = "effect_multi";
    public static final String TO_ENEMY = "to_enemy";
    
    @Override
    public PotionSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return new PotionSubType((FireEffectSubType) super.getNewData(resourceLocation, jsonObject),
                jsonObject.get(BLACKLIST),
                jsonObject.get(LIST),
                jsonObject.get(COLOR_MIXED),
                jsonObject.get(EFFECT_MULTI),
                jsonObject.get(TO_ENEMY));
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new PotionSubType(buf);
    }
}
