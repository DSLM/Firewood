package com.dslm.firewood.recipe;

import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;

public class TinderRecipeRemoveNBT
{
    public ArrayList<CompoundTag> majorEffects;
    public ArrayList<CompoundTag> minorEffects;
    
    public ItemStack cleanEffects(ItemStack stack)
    {
        CompoundTag allNBT = stack.getOrCreateTag();
        
        {
            ListTag MajorTags = (ListTag) allNBT.get(StaticValue.MAJOR);
            if(MajorTags != null)
            {
                for(CompoundTag majorRemoveTag : majorEffects)
                {
                    for(int i = 0; i < MajorTags.size(); i++)
                    {
                        if(MajorTags.get(i) instanceof CompoundTag compoundTag)
                        {
                            if(NbtUtils.compareNbt(majorRemoveTag, compoundTag, true))
                            {
                                MajorTags.remove(i);
                            }
                        }
                    }
                }
            }
            else
            {
                MajorTags = new ListTag();
            }
            allNBT.put(StaticValue.MAJOR, MajorTags);
        }
        
        {
            ListTag MinorTags = (ListTag) allNBT.get(StaticValue.MINOR);
            if(MinorTags != null)
            {
                for(CompoundTag minorRemoveTag : minorEffects)
                {
                    for(int i = 0; i < MinorTags.size(); i++)
                    {
                        if(MinorTags.get(i) instanceof CompoundTag compoundTag)
                        {
                            if(NbtUtils.compareNbt(minorRemoveTag, compoundTag, true))
                            {
                                MinorTags.set(i, FireEffectHelpers.getHelperByType(StaticValue.MINOR, compoundTag.get(StaticValue.TYPE).getAsString()).getDefaultNBT());
                            }
                        }
                    }
                }
            }
            else
            {
                MinorTags = new ListTag();
            }
            allNBT.put(StaticValue.MINOR, MinorTags);
        }
        
        stack.setTag(allNBT);
        
        return stack;
    }
    
    public static TinderRecipeRemoveNBT fromNetwork(FriendlyByteBuf buf)
    {
        return new TinderRecipeRemoveNBT(arrayFromNetwork(buf, StaticValue.MAJOR), arrayFromNetwork(buf, StaticValue.MINOR));
    }
    
    public static ArrayList<CompoundTag> arrayFromNetwork(FriendlyByteBuf buf, String kind)
    {
        ArrayList<CompoundTag> tempArray = new ArrayList<>();
        int arrSize = buf.readInt();
        for(int i = 0; i < arrSize; i++)
        {
            tempArray.add(buf.readNbt());
        }
        return tempArray;
    }
    
    public void toNetwork(FriendlyByteBuf buf)
    {
        arrayToNetwork(buf, majorEffects);
        arrayToNetwork(buf, minorEffects);
    }
    
    public static void arrayToNetwork(FriendlyByteBuf buf, ArrayList<CompoundTag> effects)
    {
        int size = effects == null ? 0 : effects.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            buf.writeNbt(effects.get(i));
        }
    }
    
    public static TinderRecipeRemoveNBT fromJSON(JsonObject obj)
    {
        if(obj == null)
            return fromJSON(null, null);
        return fromJSON(obj.getAsJsonArray(StaticValue.MAJOR), obj.getAsJsonArray(StaticValue.MINOR));
    }
    
    public static TinderRecipeRemoveNBT fromJSON(JsonArray major, JsonArray minor)
    {
        return new TinderRecipeRemoveNBT(arrayFromJSON(major, StaticValue.MAJOR), arrayFromJSON(minor, StaticValue.MINOR));
    }
    
    public static ArrayList<CompoundTag> arrayFromJSON(JsonArray array, String kind)
    {
        ArrayList<CompoundTag> tempArray = new ArrayList<>();
        if(array != null)
        {
            for(JsonElement i : array)
            {
                JsonObject tempObj = i.getAsJsonObject();
                tempArray.add(CraftingHelper.getNBT(tempObj));
            }
        }
        
        return tempArray;
    }
    
    public TinderRecipeRemoveNBT(ArrayList<CompoundTag> majorEffects, ArrayList<CompoundTag> minorEffects)
    {
        this.majorEffects = majorEffects;
        this.minorEffects = minorEffects;
    }
    
    public ArrayList<CompoundTag> getMajorEffects()
    {
        return majorEffects;
    }
    
    public void setMajorEffects(ArrayList<CompoundTag> majorEffects)
    {
        this.majorEffects = majorEffects;
    }
    
    public ArrayList<CompoundTag> getMinorEffects()
    {
        return minorEffects;
    }
    
    public void setMinorEffects(ArrayList<CompoundTag> minorEffects)
    {
        this.minorEffects = minorEffects;
    }
}
