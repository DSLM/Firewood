package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.NBTUtils.loadMajorFireData;
import static com.dslm.firewood.NBTUtils.loadMinorFireData;

public class FireEffectHelpers
{
    public static final int majorEffectColor = 0x48c774;
    public static final int minorEffectColor = 0x48c774;
    
    public static final ArrayList<String> majorEffectList = new ArrayList<>()
    {{
        add("potion");
        add("teleport");
    }};
    public static final ArrayList<String> minorEffectList = new ArrayList<>()
    {{
        add("ground");
    }};
    
    private static HashMap<String, FireEffectHelperBase> fireEffectHelpers = new HashMap<>()
    {{
        //major
        put("potion", new PotionFireEffectHelper());
        put("teleport", new TeleportFireEffectHelper());
        
        //minor
        put("ground", new GroundFireEffectHelper());
    }};
    
    public static void addHelper(String type, FireEffectHelperBase helper)
    {
        fireEffectHelpers.put(type, helper);
    }
    
    public static void addMajorHelper(String type, FireEffectHelperBase helper)
    {
        majorEffectList.add(type);
        addHelper(type, helper);
    }
    
    public static void addMinorHelper(String type, FireEffectHelperBase helper)
    {
        minorEffectList.add(type);
        addHelper(type, helper);
    }
    
    public static FireEffectHelperBase getHelperByType(String type)
    {
        return fireEffectHelpers.get(type);
    }
    
    public static int getColorByType(HashMap<String, String> data)
    {
        return getHelperByType(data.get("type")).getColor(data);
    }
    
    public static void triggerMajorEffects(ArrayList<HashMap<String, String>> majorEffects, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        // TODO: 2022/5/11 考虑继续用debuff，持续即是冷却，类似凤凰新生？再说吧
        float damage = 0f;
        for(HashMap<String, String> data : majorEffects)
        {
            var helper = getHelperByType(data.get("type"));
            damage += helper.getDamage();
        }
        damageEntity(entity, damage);
        for(HashMap<String, String> data : majorEffects)
        {
            var helper = getHelperByType(data.get("type"));
            helper.triggerEffect(data, state, level, pos, entity);
        }
    }
    
    public static void triggerMinorEffects(ArrayList<HashMap<String, String>> minorEffects, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        for(HashMap<String, String> data : minorEffects)
        {
            var helper = getHelperByType(data.get("type"));
            helper.triggerEffect(data, state, level, pos, entity);
        }
    }
    
    public static void damageEntity(LivingEntity entity, float amount)
    {
        if(amount > 0)
            entity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, amount);
    }
    
    
    public static ArrayList<Component> fireTooltips(CompoundTag compoundTag, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        
        //majorEffects
        ArrayList<HashMap<String, String>> tempMajorEffect = loadMajorFireData(compoundTag);
        if(tempMajorEffect.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.major_effect"), majorEffectColor));
            for(HashMap<String, String> data : tempMajorEffect)
            {
                ArrayList<Component> list = getHelperByType(data.get("type")).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        //minorEffects
        ArrayList<HashMap<String, String>> tempMinorEffect = loadMinorFireData(compoundTag);
        if(tempMinorEffect.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.minor_effect"), minorEffectColor));
            for(HashMap<String, String> data : tempMinorEffect)
            {
                ArrayList<Component> list = getHelperByType(data.get("type")).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        return lines;
    }
    
    
    public static TranslatableComponent colorfulText(TranslatableComponent text, int color)
    {
        return (TranslatableComponent) text.withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }
    
    public static int getMixedColor(ArrayList<HashMap<String, String>> majorEffects, ArrayList<HashMap<String, String>> minorEffects)
    {
        int num = 0;
        int[] color = {0, 0, 0};
        Color tempColor;
        
        //mainEffect
        for(HashMap<String, String> i : majorEffects)
        {
            tempColor = new Color(getColorByType(i));
            color[0] += tempColor.getRed();
            color[1] += tempColor.getGreen();
            color[2] += tempColor.getBlue();
    
            num++;
        }
    
        if(num == 0) return 0xff7500;
        Color finalColor = new Color(color[0] / num, color[1] / num, color[2] / num);
        return finalColor.getRGB();
    }
    
    public static boolean isSameNBT(String type, CompoundTag first, CompoundTag second)
    {
        return getHelperByType(type).isSameNBT(first, second);
    }
    
    public static CompoundTag saveToNBT(String type, HashMap<String, String> data)
    {
        return getHelperByType(type).saveToNBT(data);
    }
    
    public static HashMap<String, String> readFromNBT(String type, CompoundTag tags)
    {
        return getHelperByType(type).readFromNBT(tags);
    }
    
    public static CompoundTag getDefaultMinorNBTs(CompoundTag allNBT)
    {
        if(allNBT.contains("minorEffects"))
        {
            return allNBT;
        }
        
        ListTag minorTags = new ListTag();
        
        for(String type : minorEffectList)
        {
            minorTags.add(getHelperByType(type).getDefaultNBT());
        }
        
        allNBT.put("minorEffects", minorTags);
        
        return allNBT;
    }
    
    public static ItemStack addMajorEffect(ItemStack itemStack, String type, HashMap<String, String> data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get("majorEffects");
        
        CompoundTag newEffect = saveToNBT(type, data);
        //clean old same effect
        if(tags != null)
        {
            for(int i = 0; i < tags.size(); i++)
            {
                if(tags.get(i) instanceof CompoundTag compoundTag)
                {
                    if(isSameNBT(type, newEffect, compoundTag))
                    {
                        tags.remove(i);
                    }
                }
            }
        } else
        {
            tags = new ListTag();
        }
        
        tags.add(newEffect);
        allNBT.put("majorEffects", tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
    
    public static ItemStack addMinorEffect(ItemStack itemStack, String type, HashMap<String, String> data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get("minorEffects");
        
        CompoundTag newEffect = saveToNBT(type, data);
        
        //clean old same effect
        for(int i = 0; i < tags.size(); i++)
        {
            if(tags.get(i) instanceof CompoundTag compoundTag)
            {
                if(isSameNBT(type, newEffect, compoundTag))
                {
                    tags.setTag(i, newEffect);
                    break;
                }
            }
        }
        
        allNBT.put("minorEffects", tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
}
