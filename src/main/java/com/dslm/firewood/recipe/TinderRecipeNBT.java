package com.dslm.firewood.recipe;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class TinderRecipeNBT
{
    public ArrayList<FireEffectNBTData> majorEffects;
    public ArrayList<FireEffectNBTData> minorEffects;
    
    public ItemStack implementEffects(ItemStack stack)
    {
        return FireEffectHelpers.addMinorEffects(FireEffectHelpers.addMajorEffects(stack, majorEffects), minorEffects);
    }
    
    public static TinderRecipeNBT fromNetwork(FriendlyByteBuf buf)
    {
        return new TinderRecipeNBT(arrayFromNetwork(buf), arrayFromNetwork(buf));
    }
    
    public static ArrayList<FireEffectNBTData> arrayFromNetwork(FriendlyByteBuf buf)
    {
        ArrayList<FireEffectNBTData> effects = new ArrayList<>();
        int arrSize = buf.readInt();
        for(int i = 0; i < arrSize; i++)
        {
            FireEffectNBTData tempMap = new FireEffectNBTData();
            int mapSize = buf.readInt();
            for(int j = 0; j < mapSize; j++)
            {
                tempMap.put(buf.readUtf(), buf.readUtf());
            }
            effects.add(tempMap);
        }
        return effects;
    }
    
    public void toNetwork(FriendlyByteBuf buf)
    {
        arrayToNetwork(buf, majorEffects);
        arrayToNetwork(buf, minorEffects);
    }
    
    public static void arrayToNetwork(FriendlyByteBuf buf, ArrayList<FireEffectNBTData> effects)
    {
        int size = effects == null ? 0 : effects.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            FireEffectNBTData tempMap = effects.get(i);
            buf.writeInt(tempMap.size());
            for(String j : tempMap.keySet())
            {
                buf.writeUtf(j);
                buf.writeUtf(tempMap.get(j));
            }
        }
    }
    
    public FireEffectNBTData getMajorEffect(int i)
    {
        return majorEffects.get(i);
    }
    
    public FireEffectNBTData getMinorEffect(int i)
    {
        return minorEffects.get(i);
    }
    
    public static TinderRecipeNBT fromJSON(JsonObject obj)
    {
        if(obj == null)
            return fromJSON(null, null);
        return fromJSON(obj.getAsJsonArray("major"), obj.getAsJsonArray("minor"));
    }
    
    public static TinderRecipeNBT fromJSON(JsonArray major, JsonArray minor)
    {
        return new TinderRecipeNBT(arrayFromJSON(major), arrayFromJSON(minor));
    }
    
    public static ArrayList<FireEffectNBTData> arrayFromJSON(JsonArray array)
    {
        ArrayList<FireEffectNBTData> tempArray = new ArrayList<>();
        if(array != null)
        {
            for(JsonElement i : array)
            {
                JsonObject tempObj = i.getAsJsonObject();
                FireEffectNBTData tempMap = new FireEffectNBTData();
                for(String j : tempObj.keySet())
                {
                    tempMap.put(j, tempObj.get(j).getAsString());
                }
                tempArray.add(tempMap);
            }
        }
        
        return tempArray;
    }
    
    public TinderRecipeNBT(ArrayList<FireEffectNBTData> majorEffects, ArrayList<FireEffectNBTData> minorEffects)
    {
        this.majorEffects = majorEffects;
        this.minorEffects = minorEffects;
    }
    
    public void addMajorEffect(FireEffectNBTData effect)
    {
        majorEffects.add(effect);
    }
    
    public void addMinorEffect(FireEffectNBTData effect)
    {
        minorEffects.add(effect);
    }
    
    public ArrayList<FireEffectNBTData> getMajorEffects()
    {
        return majorEffects;
    }
    
    public void setMajorEffects(ArrayList<FireEffectNBTData> majorEffects)
    {
        this.majorEffects = majorEffects;
    }
    
    public ArrayList<FireEffectNBTData> getMinorEffects()
    {
        return minorEffects;
    }
    
    public void setMinorEffects(ArrayList<FireEffectNBTData> minorEffects)
    {
        this.minorEffects = minorEffects;
    }
}
