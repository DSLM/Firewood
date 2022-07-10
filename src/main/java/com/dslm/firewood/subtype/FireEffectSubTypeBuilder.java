package com.dslm.firewood.subtype;


import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonArray;
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
        if(jsonObject.get(StaticValue.TYPE) == null ||
                jsonObject.get(StaticValue.SUB_TYPE) == null ||
                jsonObject.get(StaticValue.COLOR) == null ||
                jsonObject.get(StaticValue.DAMAGE) == null ||
                jsonObject.get(StaticValue.PROCESS) == null ||
                jsonObject.get(StaticValue.RANGE) == null ||
                jsonObject.get(StaticValue.COOLDOWN) == null) return null;
    
        int[] range = new int[3];
        if(jsonObject.get(StaticValue.RANGE) instanceof JsonArray jsonArray)
        {
            for(int i = 0; i < 3; i++)
            {
                range[i] = jsonArray.get(i).getAsInt();
            }
        }
        else
        {
            int tempRange = jsonObject.get(StaticValue.RANGE).getAsInt();
        
            for(int i = 0; i < 3; i++)
            {
                range[i] = tempRange;
            }
        }
    
        return new FireEffectSubType(resourceLocation, jsonObject, range);
    }
    
    @Override
    public FireEffectSubTypeBase getNewData(FriendlyByteBuf buf)
    {
        return new FireEffectSubType(buf);
    }
}