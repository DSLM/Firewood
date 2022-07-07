package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;

import static com.dslm.firewood.util.StaticValue.TYPE;

public class FireEffectNBTStaticHelper
{
    
    public static ArrayList<FireEffectNBTDataInterface> loadMajorFireData(CompoundTag pTag)
    {
        ArrayList<FireEffectNBTDataInterface> majorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get(StaticValue.MAJOR);
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get(TYPE) != null)
                {
                    majorEffects.add(FireEffectHelpers.readFromNBT(StaticValue.MAJOR, comTag.getString(TYPE), comTag));
                }
            }
        }
        return majorEffects;
    }
    
    public static ArrayList<FireEffectNBTDataInterface> loadMinorFireData(CompoundTag pTag)
    {
        ArrayList<FireEffectNBTDataInterface> minorEffects = new ArrayList<>();
        ListTag tags = (ListTag) pTag.get(StaticValue.MINOR);
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                if(i instanceof CompoundTag comTag && comTag.get(TYPE) != null)
                {
                    minorEffects.add(FireEffectHelpers.readFromNBT(StaticValue.MINOR, comTag.getString(TYPE), comTag));
                }
            }
        }
        return minorEffects;
    }
    
    
    @SuppressWarnings("ConstantConditions")
    public static CompoundTag saveFireData(CompoundTag tag, ArrayList<FireEffectNBTDataInterface> majorEffects, ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        tag = saveMajorFireData(tag, majorEffects);
        tag = saveMinorFireData(tag, minorEffects);
        return tag;
    }
    
    public static CompoundTag saveMajorFireData(CompoundTag pTag, ArrayList<FireEffectNBTDataInterface> majorEffects)
    {
        ListTag tags = new ListTag();
        for(FireEffectNBTDataInterface i : majorEffects)
        {
            if(i.getType() != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(StaticValue.MAJOR, i.getType(), i));
            }
        }
        pTag.put(StaticValue.MAJOR, tags);
        
        return pTag;
    }
    
    public static CompoundTag saveMinorFireData(CompoundTag pTag, ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        ListTag tags = new ListTag();
        for(FireEffectNBTDataInterface i : minorEffects)
        {
            if(i.getType() != null)
            {
                tags.add(FireEffectHelpers.saveToNBT(StaticValue.MINOR, i.getType(), i));
            }
        }
        pTag.put(StaticValue.MINOR, tags);
        
        return pTag;
    }
}
