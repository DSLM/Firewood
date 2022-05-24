package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;

public class FireEffectNBTHelper
{
    public static final String MAJOR = "majorEffects";
    public static final String MINOR = "minorEffects";
    
    public static ArrayList<FireEffectNBTData> loadMajorFireData(CompoundTag pTag)
    {
        ArrayList<FireEffectNBTData> majorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get(MAJOR);
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get(TYPE) != null)
                {
                    majorEffects.add(FireEffectHelpers.readFromNBT(MAJOR, comTag.getString(TYPE), comTag));
                }
            }
        }
        return majorEffects;
    }
    
    public static ArrayList<FireEffectNBTData> loadMinorFireData(CompoundTag pTag)
    {
        ArrayList<FireEffectNBTData> minorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get(MINOR);
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get(TYPE) != null)
                {
                    minorEffects.add(FireEffectHelpers.readFromNBT(MINOR, comTag.getString(TYPE), comTag));
                }
            }
        }
        return minorEffects;
    }
    
    
    @SuppressWarnings("ConstantConditions")
    public static CompoundTag saveFireData(CompoundTag tag, ArrayList<FireEffectNBTData> majorEffects, ArrayList<FireEffectNBTData> minorEffects)
    {
        tag = saveMajorFireData(tag, majorEffects);
        tag = saveMinorFireData(tag, minorEffects);
        return tag;
    }
    
    public static CompoundTag saveMajorFireData(CompoundTag pTag, ArrayList<FireEffectNBTData> majorEffects)
    {
        ListTag tags = new ListTag();
        for(FireEffectNBTData i : majorEffects)
        {
            if(i.getType() != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(MAJOR, i.getType(), i));
            }
        }
        pTag.put(MAJOR, tags);
        
        return pTag;
    }
    
    public static CompoundTag saveMinorFireData(CompoundTag pTag, ArrayList<FireEffectNBTData> minorEffects)
    {
        ListTag tags = new ListTag();
        for(FireEffectNBTData i : minorEffects)
        {
            if(i.getType() != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(MINOR, i.getType(), i));
            }
        }
        pTag.put(MINOR, tags);
        
        return pTag;
    }
}
