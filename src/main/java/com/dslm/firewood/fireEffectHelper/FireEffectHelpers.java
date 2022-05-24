package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.Register;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.config.SpiritualFireBlockEffectConfig;
import com.dslm.firewood.fireEffectHelper.block.*;
import com.dslm.firewood.fireEffectHelper.block.baseClass.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.block.baseClass.MajorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.block.baseClass.MinorFireEffectHelperInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.block.baseClass.FireNBTHelper.loadMajorFireData;
import static com.dslm.firewood.fireEffectHelper.block.baseClass.FireNBTHelper.loadMinorFireData;

public class FireEffectHelpers
{
    public static final String major = "majorEffects";
    public static final String minor = "minorEffects";
    
    public static final int majorEffectColor = 0x48c774;
    public static final int minorEffectColor = 0x48c774;
    
    public static ExceptionCatchHelper exceptionCatchHelper = new ExceptionCatchHelper();
    
    public static final FireEffectKindHelper<MajorFireEffectHelperInterface> majorEffectHelpers = new FireEffectKindHelper(major);
    public static final FireEffectKindHelper<MinorFireEffectHelperInterface> minorEffectHelpers = new FireEffectKindHelper(minor);
    
    static
    {
        majorEffectHelpers.helpers.put("potion", new PotionFireEffectHelper("potion"));
        majorEffectHelpers.helpers.put("teleport", new TeleportFireEffectHelper("teleport"));
        majorEffectHelpers.helpers.put("smelter", new SmelterFireEffectHelper("smelter"));
        
        
        minorEffectHelpers.helpers.put("ground", new GroundFireEffectHelper("ground"));
    }
    
    
    public static void addHelper(String kind, String type, MajorFireEffectHelperInterface helper)
    {
        if(major.equals(kind)) addMajorHelper(type, helper);
        if(minor.equals(kind)) addMajorHelper(type, helper);
    }
    
    public static void addMajorHelper(String type, MajorFireEffectHelperInterface helper)
    {
        majorEffectHelpers.addHelper(type, helper);
    }
    
    public static void addMinorHelper(String type, MinorFireEffectHelperInterface helper)
    {
        minorEffectHelpers.addHelper(type, helper);
    }
    
    public static FireEffectHelperInterface getHelperByType(String kind, String type)
    {
        if(major.equals(kind)) return getMajorHelperByType(type);
        if(minor.equals(kind)) return getMinorHelperByType(type);
        return exceptionCatchHelper;
    }
    
    public static MajorFireEffectHelperInterface getMajorHelperByType(String type)
    {
        if(majorEffectHelpers.helpers.containsKey(type))
            return majorEffectHelpers.helpers.get(type);
        return exceptionCatchHelper;
    }
    
    public static MinorFireEffectHelperInterface getMinorHelperByType(String type)
    {
        if(minorEffectHelpers.helpers.containsKey(type))
            return minorEffectHelpers.helpers.get(type);
        return exceptionCatchHelper;
    }
    
    public static int getColorByType(String kind, HashMap<String, String> data)
    {
        return getHelperByType(kind, data.get("type")).getColor(data);
    }
    
    public static ArrayList<HashMap<String, String>> triggerMajorEffects(ArrayList<HashMap<String, String>> majorEffects,
                                                                         ArrayList<HashMap<String, String>> minorEffects,
                                                                         BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        float damage = 0f;
        float minHealth = 0f;
        for(HashMap<String, String> data : majorEffects)
        {
            var helper = getMajorHelperByType(data.get("type"));
            damage += helper.getDamage(data);
            minHealth += helper.getMinHealth(data);
        }
        float nowHealth = entity.getHealth();
        damageEntity(entity, damage);
        if(nowHealth < minHealth)
        {
            return majorEffects;
        }
        for(int i = 0; i < majorEffects.size(); i++)
        {
            HashMap<String, String> data = majorEffects.get(i);
            var helper = getMajorHelperByType(data.get("type"));
            data = helper.triggerEffect(data, state, level, pos, entity, majorEffects, minorEffects);
            majorEffects.set(i, data);
        }
        return majorEffects;
    }
    
    public static ArrayList<HashMap<String, String>> triggerMinorEffects(ArrayList<HashMap<String, String>> majorEffects,
                                                                         ArrayList<HashMap<String, String>> minorEffects,
                                                                         BlockState state, Level level, BlockPos pos)
    {
        for(int i = 0; i < minorEffects.size(); i++)
        {
            HashMap<String, String> data = minorEffects.get(i);
            var helper = getMinorHelperByType(data.get("type"));
            data = helper.triggerEffect(data, state, level, pos, majorEffects, minorEffects);
            minorEffects.set(i, data);
        }
        return minorEffects;
    }
    
    
    public static void damageEntity(LivingEntity entity, float amount)
    {
        damageEntity(entity, amount, SpiritualFireBlockEffectConfig.FIRED_FLESH_TIME.get());
    }
    
    public static void damageEntity(LivingEntity entity, float amount, int cooldown)
    {
        if(amount == 0)
            return;
    
        entity.getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(
                playerSpiritualDamage -> playerSpiritualDamage.setFleshDamage(amount));
    
        entity.addEffect(new MobEffectInstance(Register.FIRED_FLESH.get(), cooldown, 99));
    
        // TODO: 2022/5/9 自行实现火焰遮挡视线

//                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
//                if(entity.getRemainingFireTicks() == 0)
//                {
//                    entity.setSecondsOnFire(8);
//                }
    }
    
    public static ArrayList<Component> fireTooltips(ArrayList<HashMap<String, String>> majorEffects,
                                                    ArrayList<HashMap<String, String>> minorEffects, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        
        //majorEffects
        if(majorEffects.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.major_effect"), majorEffectColor));
            for(HashMap<String, String> data : majorEffects)
            {
                ArrayList<Component> list = getMajorHelperByType(data.get("type")).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        //minorEffects
        if(minorEffects.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.minor_effect"), minorEffectColor));
            for(HashMap<String, String> data : minorEffects)
            {
                ArrayList<Component> list = getMinorHelperByType(data.get("type")).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        return lines;
    }
    
    public static ArrayList<Component> fireTooltips(CompoundTag compoundTag, boolean extended)
    {
        ArrayList<HashMap<String, String>> majorEffects = loadMajorFireData(compoundTag);
        ArrayList<HashMap<String, String>> minorEffects = loadMinorFireData(compoundTag);
        return fireTooltips(majorEffects, minorEffects, extended);
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
            tempColor = new Color(getColorByType(major, i));
            color[0] += tempColor.getRed();
            color[1] += tempColor.getGreen();
            color[2] += tempColor.getBlue();
    
            num++;
        }
    
        if(num == 0) return 0xff7500;
        Color finalColor = new Color(color[0] / num, color[1] / num, color[2] / num);
        return finalColor.getRGB();
    }
    
    public static boolean isSameNBT(String kind, String type, CompoundTag first, CompoundTag second)
    {
        return getHelperByType(kind, type).isSameNBT(first, second);
    }
    
    public static CompoundTag saveToNBT(String kind, String type, HashMap<String, String> data)
    {
        return getHelperByType(kind, type).saveToNBT(data);
    }
    
    public static HashMap<String, String> readFromNBT(String kind, String type, CompoundTag tags)
    {
        return getHelperByType(kind, type).readFromNBT(tags);
    }
    
    public static CompoundTag getDefaultMinorNBTs(CompoundTag allNBT)
    {
        if(allNBT.contains(minor))
        {
            return allNBT;
        }
        
        ListTag minorTags = new ListTag();
    
        for(String type : minorEffectHelpers.helpers.keySet())
        {
            minorTags.add(getMinorHelperByType(type).getDefaultNBT());
        }
    
        allNBT.put(minor, minorTags);
        
        return allNBT;
    }
    
    public static ItemStack addMajorEffect(ItemStack itemStack, String type, HashMap<String, String> data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get(major);
    
        CompoundTag newEffect = saveToNBT(major, type, data);
        //clean old same effect
        if(tags != null)
        {
            for(int i = 0; i < tags.size(); i++)
            {
                if(tags.get(i) instanceof CompoundTag compoundTag)
                {
                    if(isSameNBT(major, type, newEffect, compoundTag))
                    {
                        tags.remove(i);
                    }
                }
            }
        }
        else
        {
            tags = new ListTag();
        }
    
        tags.add(newEffect);
        allNBT.put(major, tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
    
    public static ItemStack addMinorEffect(ItemStack itemStack, String type, HashMap<String, String> data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get(minor);
    
        CompoundTag newEffect = saveToNBT(minor, type, data);
        
        //clean old same effect
        for(int i = 0; i < tags.size(); i++)
        {
            if(tags.get(i) instanceof CompoundTag compoundTag)
            {
                if(isSameNBT(minor, type, newEffect, compoundTag))
                {
                    tags.setTag(i, newEffect);
                    break;
                }
            }
        }
    
        allNBT.put(minor, tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
    
    public static ItemStack addMajorEffects(ItemStack itemStack, List<HashMap<String, String>> data)
    {
        for(HashMap<String, String> one : data)
        {
            addMajorEffect(itemStack, one.get("type"), one);
        }
        return itemStack;
    }
    
    public static ItemStack addMinorEffects(ItemStack itemStack, List<HashMap<String, String>> data)
    {
        for(HashMap<String, String> one : data)
        {
            addMinorEffect(itemStack, one.get("type"), one);
        }
        return itemStack;
    }
    
    public static String getJEIType(ItemStack itemStack)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        ArrayList<HashMap<String, String>> majorEffects = loadMajorFireData(itemStack.getOrCreateTag());
        ArrayList<HashMap<String, String>> minorEffects = loadMinorFireData(itemStack.getOrCreateTag());
        
        for(HashMap<String, String> one : majorEffects)
        {
            stringBuilder.append(getMajorHelperByType(one.get("type")).getJEIString(one)).append(";");
        }
        for(HashMap<String, String> one : minorEffects)
        {
            stringBuilder.append(getMinorHelperByType(one.get("type")).getJEIString(one)).append(";");
        }
    
        return stringBuilder.toString();
    }
    
    public static void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        majorEffectHelpers.helpers.forEach((s, helper) -> helper.fillItemCategory(items, item));
        minorEffectHelpers.helpers.forEach((s, helper) -> helper.fillItemCategory(items, item));
    }
    
    public static boolean canBePlacedOn(Level level, BlockPos pos, CompoundTag tag)
    {
        ArrayList<HashMap<String, String>> minorEffects = loadMinorFireData(tag);
        
        for(HashMap<String, String> one : minorEffects)
        {
            if(!getMinorHelperByType(one.get("type")).canBePlacedOn(one, level, pos))
            {
                return false;
            }
        }
        
        return true;
    }
}
