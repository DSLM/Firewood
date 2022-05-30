package com.dslm.firewood.fireEffectHelper.flesh.data;


import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import static com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectSubType.*;

public class FireEffectSubTypeBuilder
{
    public FireEffectSubTypeBuilder()
    {
    
    }
    
    public FireEffectSubType getNewData(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        return jsonObject.get(ID) == null ||
                jsonObject.get(SUB_ID) == null ||
                jsonObject.get(COLOR) == null ||
                jsonObject.get(DAMAGE) == null ||
                jsonObject.get(MIN_HEALTH) == null ||
                jsonObject.get(PROCESS) == null ||
                jsonObject.get(CHANCE) == null ||
                jsonObject.get(RANGE) == null ?
                null :
                new FireEffectSubType(resourceLocation, jsonObject);
    }
}