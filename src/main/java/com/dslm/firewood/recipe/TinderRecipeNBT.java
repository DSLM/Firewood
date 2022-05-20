package com.dslm.firewood.recipe;

import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class TinderRecipeNBT
{
    public ArrayList<HashMap<String, String>> majorEffects;
    public ArrayList<HashMap<String, String>> minorEffects;
    
    public ItemStack implementEffects(ItemStack stack)
    {
        return FireEffectHelpers.addMinorEffects(FireEffectHelpers.addMajorEffects(stack, majorEffects), minorEffects);
    }
    
    public static TinderRecipeNBT fromNetwork(FriendlyByteBuf buf)
    {
        return new TinderRecipeNBT(arrayFromNetwork(buf), arrayFromNetwork(buf));
    }
    
    public static ArrayList<HashMap<String, String>> arrayFromNetwork(FriendlyByteBuf buf)
    {
        ArrayList<HashMap<String, String>> effects = new ArrayList<>();
        int arrSize = buf.readInt();
        for(int i = 0; i < arrSize; i++)
        {
            HashMap<String, String> tempMap = new HashMap<>();
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
    
    public static void arrayToNetwork(FriendlyByteBuf buf, ArrayList<HashMap<String, String>> effects)
    {
        int size = effects == null ? 0 : effects.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            HashMap<String, String> tempMap = effects.get(i);
            buf.writeInt(tempMap.size());
            for(String j : tempMap.keySet())
            {
                buf.writeUtf(j);
                buf.writeUtf(tempMap.get(j));
            }
        }
    }
    
    public HashMap<String, String> getMajorEffect(int i)
    {
        return majorEffects.get(i);
    }
    
    public HashMap<String, String> getMinorEffect(int i)
    {
        return minorEffects.get(i);
    }
    
    public static TinderRecipeNBT fromJSON(JsonObject obj)
    {
        return fromJSON(obj.getAsJsonArray("major"), obj.getAsJsonArray("minor"));
    }
    
    public static TinderRecipeNBT fromJSON(JsonArray major, JsonArray minor)
    {
        return new TinderRecipeNBT(arrayFromJSON(major), arrayFromJSON(minor));
    }
    
    public static ArrayList<HashMap<String, String>> arrayFromJSON(JsonArray array)
    {
        ArrayList<HashMap<String, String>> tempArray = new ArrayList<>();
        if(array != null)
        {
            for(JsonElement i : array)
            {
                JsonObject tempObj = i.getAsJsonObject();
                HashMap<String, String> tempMap = new HashMap<String, String>();
                for(String j : tempObj.keySet())
                {
                    tempMap.put(j, tempObj.get(j).getAsString());
                }
                tempArray.add(tempMap);
            }
        }
        
        return tempArray;
    }
    
    public TinderRecipeNBT(ArrayList<HashMap<String, String>> majorEffects, ArrayList<HashMap<String, String>> minorEffects)
    {
        this.majorEffects = majorEffects;
        this.minorEffects = minorEffects;
    }
    
    public void addMajorEffect(HashMap<String, String> effect)
    {
        majorEffects.add(effect);
    }
    
    public void addMinorEffect(HashMap<String, String> effect)
    {
        minorEffects.add(effect);
    }
    
    public ArrayList<HashMap<String, String>> getMajorEffects()
    {
        return majorEffects;
    }
    
    public void setMajorEffects(ArrayList<HashMap<String, String>> majorEffects)
    {
        this.majorEffects = majorEffects;
    }
    
    public ArrayList<HashMap<String, String>> getMinorEffects()
    {
        return minorEffects;
    }
    
    public void setMinorEffects(ArrayList<HashMap<String, String>> minorEffects)
    {
        this.minorEffects = minorEffects;
    }
}
