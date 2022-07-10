package com.dslm.firewood.recipe;

import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class TinderRecipeAddNBT
{
    public ArrayList<FireEffectNBTDataInterface> majorEffects;
    public ArrayList<FireEffectNBTDataInterface> minorEffects;
    
    public ItemStack implementEffects(ItemStack stack)
    {
        return FireEffectHelpers.addMinorEffects(FireEffectHelpers.addMajorEffects(stack, majorEffects), minorEffects);
    }
    
    public static TinderRecipeAddNBT fromNetwork(FriendlyByteBuf buf)
    {
        return new TinderRecipeAddNBT(arrayFromNetwork(buf, StaticValue.MAJOR), arrayFromNetwork(buf, StaticValue.MINOR));
    }
    
    public static ArrayList<FireEffectNBTDataInterface> arrayFromNetwork(FriendlyByteBuf buf, String kind)
    {
        ArrayList<FireEffectNBTDataInterface> tempArray = new ArrayList<>();
        int arrSize = buf.readInt();
        for(int i = 0; i < arrSize; i++)
        {
            String type = buf.readUtf();
            FireEffectNBTDataInterface tempMap = FireEffectHelpers.getHelperByType(kind, type).getDefaultData();
            tempArray.add(tempMap.fromNetwork(buf));
        }
        return tempArray;
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
            buf.writeUtf(effects.get(i).getType());
            effects.get(i).toNetwork(buf);
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
    
    public static TinderRecipeAddNBT fromJSON(JsonObject obj)
    {
        if(obj == null)
            return fromJSON(null, null);
        return fromJSON(obj.getAsJsonArray(StaticValue.MAJOR), obj.getAsJsonArray(StaticValue.MINOR));
    }
    
    public static TinderRecipeAddNBT fromJSON(JsonArray major, JsonArray minor)
    {
        return new TinderRecipeAddNBT(arrayFromJSON(major, StaticValue.MAJOR), arrayFromJSON(minor, StaticValue.MINOR));
    }
    
    public static ArrayList<FireEffectNBTDataInterface> arrayFromJSON(JsonArray array, String kind)
    {
        ArrayList<FireEffectNBTDataInterface> tempArray = new ArrayList<>();
        if(array != null)
        {
            for(JsonElement i : array)
            {
                JsonObject tempObj = i.getAsJsonObject();
                String type = tempObj.get(StaticValue.TYPE).getAsString();
                FireEffectNBTDataInterface tempMap = FireEffectHelpers.getHelperByType(kind, type).getDefaultData();
                tempArray.add(tempMap.fromJSON(tempObj));
            }
        }
        
        return tempArray;
    }
    
    public TinderRecipeAddNBT(ArrayList<FireEffectNBTDataInterface> majorEffects, ArrayList<FireEffectNBTDataInterface> minorEffects)
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
