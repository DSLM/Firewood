package com.dslm.firewood.fireEffectHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.HashMap;

public class FireNBTHelper
{
    
    public static ArrayList<HashMap<String, String>> loadMajorFireData(CompoundTag pTag)
    {
        ArrayList<HashMap<String, String>> majorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get("majorEffects");
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get("type") != null)
                {
                    majorEffects.add(FireEffectHelpers.readFromNBT(comTag.getString("type"), comTag));
                }
            }
        }
        return majorEffects;
    }
    
    public static ArrayList<HashMap<String, String>> loadMinorFireData(CompoundTag pTag)
    {
        ArrayList<HashMap<String, String>> minorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get("minorEffects");
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get("type") != null)
                {
                    minorEffects.add(FireEffectHelpers.readFromNBT(comTag.getString("type"), comTag));
                }
            }
        }
        return minorEffects;
    }
    
    
    @SuppressWarnings("ConstantConditions")
    public static CompoundTag saveFireData(CompoundTag pTag, ArrayList<HashMap<String, String>> majorEffects, ArrayList<HashMap<String, String>> minorEffects)
    {
        pTag = saveMajorFireData(pTag, majorEffects);
        pTag = saveMinorFireData(pTag, minorEffects);
        return pTag;
    }
    
    public static CompoundTag saveMajorFireData(CompoundTag pTag, ArrayList<HashMap<String, String>> majorEffects)
    {
        ListTag tags = new ListTag();
        for(HashMap<String, String> i : majorEffects)
        {
            if(i.get("type") != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(i.get("type"), i));
            }
        }
        pTag.put("majorEffects", tags);
        
        return pTag;
    }
    
    public static CompoundTag saveMinorFireData(CompoundTag pTag, ArrayList<HashMap<String, String>> minorEffects)
    {
        ListTag tags = new ListTag();
        for(HashMap<String, String> i : minorEffects)
        {
            if(i.get("type") != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(i.get("type"), i));
            }
        }
        pTag.put("minorEffects", tags);
        
        return pTag;
    }
}
