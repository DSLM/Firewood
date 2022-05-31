package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.Register;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.config.SpiritualFireBlockEffectConfig;
import com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MajorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MinorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.util.StaticValue;
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
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTHelper.loadMajorFireData;
import static com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTHelper.loadMinorFireData;

public class FireEffectHelpers
{
    
    public static ExceptionCatchHelper exceptionCatchHelper = new ExceptionCatchHelper();
    
    public static final FireEffectKindHelper<MajorFireEffectHelperInterface> majorEffectHelpers = new FireEffectKindHelper(StaticValue.MAJOR);
    public static final FireEffectKindHelper<MinorFireEffectHelperInterface> minorEffectHelpers = new FireEffectKindHelper(StaticValue.MINOR);
    
    static
    {
        majorEffectHelpers.helpers.put("potion", new PotionFireEffectHelper("potion"));
        majorEffectHelpers.helpers.put("teleport", new TeleportFireEffectHelper("teleport"));
        majorEffectHelpers.helpers.put("smelter", new SmelterFireEffectHelper("smelter"));
        majorEffectHelpers.helpers.put("block_to_block", new BlockToBlockFireEffectHelper("block_to_block"));
        
        
        minorEffectHelpers.helpers.put("ground", new GroundFireEffectHelper("ground"));
    }
    
    
    public static void addHelper(String kind, String type, MajorFireEffectHelperInterface helper)
    {
        if(StaticValue.MAJOR.equals(kind)) addMajorHelper(type, helper);
        if(StaticValue.MINOR.equals(kind)) addMajorHelper(type, helper);
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
        if(StaticValue.MAJOR.equals(kind)) return getMajorHelperByType(type);
        if(StaticValue.MINOR.equals(kind)) return getMinorHelperByType(type);
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
    
    public static int getColorByType(String kind, FireEffectNBTData data)
    {
        return getHelperByType(kind, data.getType()).getColor(data);
    }
    
    public static ArrayList<FireEffectNBTData> triggerMajorEffects(ArrayList<FireEffectNBTData> majorEffects,
                                                                   ArrayList<FireEffectNBTData> minorEffects,
                                                                   BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        float damage = 0f;
        float minHealth = 0f;
        for(FireEffectNBTData data : majorEffects)
        {
            var helper = getMajorHelperByType(data.getType());
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
            FireEffectNBTData data = majorEffects.get(i);
            var helper = getMajorHelperByType(data.getType());
            data = helper.triggerEffect(data, state, level, pos, entity, majorEffects, minorEffects);
            majorEffects.set(i, data);
        }
        return majorEffects;
    }
    
    public static ArrayList<FireEffectNBTData> triggerMinorEffects(ArrayList<FireEffectNBTData> majorEffects,
                                                                   ArrayList<FireEffectNBTData> minorEffects,
                                                                   BlockState state, Level level, BlockPos pos)
    {
        for(int i = 0; i < minorEffects.size(); i++)
        {
            FireEffectNBTData data = minorEffects.get(i);
            var helper = getMinorHelperByType(data.getType());
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
    
    public static ArrayList<Component> fireTooltips(ArrayList<FireEffectNBTData> majorEffects,
                                                    ArrayList<FireEffectNBTData> minorEffects, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        
        //majorEffects
        if(majorEffects.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.major_effect"), StaticValue.MAJOR_EFFECT_COLOR));
            for(FireEffectNBTData data : majorEffects)
            {
                ArrayList<Component> list = getMajorHelperByType(data.getType()).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        //minorEffects
        if(minorEffects.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.minor_effect"), StaticValue.MINOR_EFFECT_COLOR));
            for(FireEffectNBTData data : minorEffects)
            {
                ArrayList<Component> list = getMinorHelperByType(data.getType()).getToolTips(data, extended);
                lines.addAll(list);
            }
        }
        
        return lines;
    }
    
    public static ArrayList<Component> fireTooltips(CompoundTag compoundTag, boolean extended)
    {
        ArrayList<FireEffectNBTData> majorEffects = loadMajorFireData(compoundTag);
        ArrayList<FireEffectNBTData> minorEffects = loadMinorFireData(compoundTag);
        return fireTooltips(majorEffects, minorEffects, extended);
    }
    
    
    public static TranslatableComponent colorfulText(TranslatableComponent text, int color)
    {
        return (TranslatableComponent) text.withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }
    
    public static int getMixedColor(ArrayList<FireEffectNBTData> majorEffects, ArrayList<FireEffectNBTData> minorEffects)
    {
        int num = 0;
        int[] color = {0, 0, 0};
        Color tempColor;
        
        //mainEffect
        for(FireEffectNBTData i : majorEffects)
        {
            int colorInt = getColorByType(StaticValue.MAJOR, i);
            if(colorInt < 0 || colorInt > 0xffffff)
            {
                continue;
            }
            tempColor = new Color(colorInt);
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
    
    public static CompoundTag saveToNBT(String kind, String type, FireEffectNBTData data)
    {
        return getHelperByType(kind, type).saveToNBT(data);
    }
    
    public static FireEffectNBTData readFromNBT(String kind, String type, CompoundTag tags)
    {
        return getHelperByType(kind, type).readFromNBT(tags);
    }
    
    public static CompoundTag getDefaultMinorNBTs(CompoundTag allNBT)
    {
        if(allNBT.contains(StaticValue.MINOR))
        {
            return allNBT;
        }
        
        ListTag minorTags = new ListTag();
    
        for(String type : minorEffectHelpers.helpers.keySet())
        {
            minorTags.add(getMinorHelperByType(type).getDefaultNBT());
        }
    
        allNBT.put(StaticValue.MINOR, minorTags);
        
        return allNBT;
    }
    
    public static ItemStack addMajorEffect(ItemStack itemStack, String type, FireEffectNBTData data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get(StaticValue.MAJOR);
    
        CompoundTag newEffect = saveToNBT(StaticValue.MAJOR, type, data);
        //clean old same effect
        if(tags != null)
        {
            for(int i = 0; i < tags.size(); i++)
            {
                if(tags.get(i) instanceof CompoundTag compoundTag)
                {
                    if(isSameNBT(StaticValue.MAJOR, type, newEffect, compoundTag))
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
        allNBT.put(StaticValue.MAJOR, tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
    
    public static ItemStack addMinorEffect(ItemStack itemStack, String type, FireEffectNBTData data)
    {
        CompoundTag allNBT = getDefaultMinorNBTs(itemStack.getOrCreateTag());
        ListTag tags = (ListTag) allNBT.get(StaticValue.MINOR);
    
        CompoundTag newEffect = saveToNBT(StaticValue.MINOR, type, data);
        
        //clean old same effect
        for(int i = 0; i < tags.size(); i++)
        {
            if(tags.get(i) instanceof CompoundTag compoundTag)
            {
                if(isSameNBT(StaticValue.MINOR, type, newEffect, compoundTag))
                {
                    tags.setTag(i, newEffect);
                    break;
                }
            }
        }
    
        allNBT.put(StaticValue.MINOR, tags);
        itemStack.setTag(allNBT);
        return itemStack;
    }
    
    public static ItemStack addMajorEffects(ItemStack itemStack, List<FireEffectNBTData> data)
    {
        for(FireEffectNBTData one : data)
        {
            addMajorEffect(itemStack, one.getType(), one);
        }
        return itemStack;
    }
    
    public static ItemStack addMinorEffects(ItemStack itemStack, List<FireEffectNBTData> data)
    {
        for(FireEffectNBTData one : data)
        {
            addMinorEffect(itemStack, one.getType(), one);
        }
        return itemStack;
    }
    
    public static String getJEIType(ItemStack itemStack)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        ArrayList<FireEffectNBTData> majorEffects = loadMajorFireData(itemStack.getOrCreateTag());
        ArrayList<FireEffectNBTData> minorEffects = loadMinorFireData(itemStack.getOrCreateTag());
    
        for(FireEffectNBTData one : majorEffects)
        {
            stringBuilder.append(getMajorHelperByType(one.getType()).getJEIString(one)).append(";");
        }
        for(FireEffectNBTData one : minorEffects)
        {
            stringBuilder.append(getMinorHelperByType(one.getType()).getJEIString(one)).append(";");
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
        ArrayList<FireEffectNBTData> minorEffects = loadMinorFireData(tag);
    
        for(FireEffectNBTData one : minorEffects)
        {
            if(!getMinorHelperByType(one.getType()).canBePlacedOn(one, level, pos))
            {
                return false;
            }
        }
        
        return true;
    }
}
