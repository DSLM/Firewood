package com.dslm.firewood;

import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.Firewood.LOGGER;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.getColorByType;

public class NBTUtils
{
    
    public static ArrayList<HashMap<String, String>> loadMajorFireData(CompoundTag pTag)
    {
        ArrayList<HashMap<String, String>> majorEffects = new ArrayList<HashMap<String, String>>();
        ListTag tags = (ListTag) pTag.get("majorEffects");
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                HashMap<String, String> item = new HashMap<String, String>();
                if(((CompoundTag) i).get("type") != null)
                {
                    item.put("type", ((CompoundTag) i).get("type").getAsString());
                    switch(item.get("type"))
                    {
                        case "potion":
                            item.put("potion", ((CompoundTag) i).get("potion").getAsString());
                            item.put("name", ((CompoundTag) i).get("name").getAsString());
                            break;
                        default:
                            break;
                    }
                }
                majorEffects.add(item);
            }
        }
        return majorEffects;
    }
    
    public static ArrayList<HashMap<String, String>> loadMinorFireData(CompoundTag pTag)
    {
        ArrayList<HashMap<String, String>> minorEffects = new ArrayList<HashMap<String, String>>();
        ListTag tags = (ListTag) pTag.get("minorEffects");
        if(tags != null)
        {
            for(Tag i : tags.stream().toList())
            {
                HashMap<String, String> item = new HashMap<String, String>();
                if(((CompoundTag) i).get("type") != null)
                {
                    item.put("type", ((CompoundTag) i).get("type").getAsString());
                    switch(item.get("type"))
                    {
                        default:
                            break;
                    }
                }
                minorEffects.add(item);
            }
        }
        return minorEffects;
    }
    
    
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
            CompoundTag newEffect = new CompoundTag();
            newEffect.putString("type", i.get("type"));
            switch(i.get("type"))
            {
                case "potion":
                    newEffect.putString("name", i.get("name"));
                    newEffect.putString("potion", i.get("potion"));
                    break;
                default:
                    break;
            }
            tags.add(newEffect);
        }
        pTag.put("majorEffects", tags);
        
        return pTag;
    }
    
    public static CompoundTag saveMinorFireData(CompoundTag pTag, ArrayList<HashMap<String, String>> minorEffects)
    {
        ListTag tags = new ListTag();
        for(HashMap<String, String> i : minorEffects)
        {
            CompoundTag newEffect = new CompoundTag();
            newEffect.putString("type", i.get("type"));
            switch(i.get("type"))
            {
                default:
                    break;
            }
            tags.add(newEffect);
        }
        pTag.put("minorEffects", tags);
        
        return pTag;
    }
}
