package com.dslm.firewood.recipe;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class TinderRecipeNBT
{
    public ArrayList<FireEffectNBTDataInterface> majorEffects;
    public ArrayList<FireEffectNBTDataInterface> minorEffects;
    
    public ItemStack implementEffects(ItemStack stack)
    {
        return FireEffectHelpers.addMinorEffects(FireEffectHelpers.addMajorEffects(stack, majorEffects), minorEffects);
    }
    
    public static TinderRecipeNBT fromNetwork(FriendlyByteBuf buf)
    {
        return new TinderRecipeNBT(arrayFromNetwork(buf), arrayFromNetwork(buf));
    }
    
    public static ArrayList<FireEffectNBTDataInterface> arrayFromNetwork(FriendlyByteBuf buf)
    {
        ArrayList<FireEffectNBTDataInterface> effects = new ArrayList<>();
        int arrSize = buf.readInt();
        for(int i = 0; i < arrSize; i++)
        {
            FireEffectNBTDataInterface tempMap = new FireEffectNBTData();
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
    
    public static void arrayToNetwork(FriendlyByteBuf buf, ArrayList<FireEffectNBTDataInterface> effects)
    {
        int size = effects == null ? 0 : effects.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            FireEffectNBTDataInterface tempMap = effects.get(i);
            buf.writeInt(tempMap.size());
            for(String j : tempMap.keySet())
            {
                buf.writeUtf(j);
                buf.writeUtf(tempMap.get(j));
            }
        }
    }
    
    public FireEffectNBTDataInterface getMajorEffect(int i)
    {
        return majorEffects.get(i);
    }
    
    public FireEffectNBTDataInterface getMinorEffect(int i)
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
    
    public static ArrayList<FireEffectNBTDataInterface> arrayFromJSON(JsonArray array)
    {
        ArrayList<FireEffectNBTDataInterface> tempArray = new ArrayList<>();
        if(array != null)
        {
            for(JsonElement i : array)
            {
                JsonObject tempObj = i.getAsJsonObject();
                FireEffectNBTDataInterface tempMap = new FireEffectNBTData();
                for(String j : tempObj.keySet())
                {
                    tempMap.put(j, tempObj.get(j).getAsString());
                }
                tempArray.add(tempMap);
            }
        }
        
        return tempArray;
    }
    
    public TinderRecipeNBT(ArrayList<FireEffectNBTDataInterface> majorEffects, ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        this.majorEffects = majorEffects;
        this.minorEffects = minorEffects;
    }
    
    public void addMajorEffect(FireEffectNBTDataInterface effect)
    {
        majorEffects.add(effect);
    }
    
    public void addMinorEffect(FireEffectNBTDataInterface effect)
    {
        minorEffects.add(effect);
    }
    
    public ArrayList<FireEffectNBTDataInterface> getMajorEffects()
    {
        return majorEffects;
    }
    
    public void setMajorEffects(ArrayList<FireEffectNBTDataInterface> majorEffects)
    {
        this.majorEffects = majorEffects;
    }
    
    public ArrayList<FireEffectNBTDataInterface> getMinorEffects()
    {
        return minorEffects;
    }
    
    public void setMinorEffects(ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        this.minorEffects = minorEffects;
    }
}
