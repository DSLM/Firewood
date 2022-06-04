package com.dslm.firewood.subType;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface FireEffectSubTypeBuilderBase
{
    FireEffectSubTypeBase getNewData(ResourceLocation resourceLocation, JsonObject jsonObject);
    
    FireEffectSubTypeBase getNewData(FriendlyByteBuf buf);
}
