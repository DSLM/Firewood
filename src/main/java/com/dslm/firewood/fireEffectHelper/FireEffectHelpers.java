package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.Register;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.config.SpiritualFireBlockEffectConfig;
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

import static com.dslm.firewood.fireEffectHelper.FireNBTHelper.loadMajorFireData;
import static com.dslm.firewood.fireEffectHelper.FireNBTHelper.loadMinorFireData;

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
    
    public static ExceptionCatchHelper exceptionCatchHelper = new ExceptionCatchHelper();
    
    private static HashMap<String, FireEffectHelperInterface> fireEffectHelpers = new HashMap<>()
    {{
        //major
        put("potion", new PotionFireEffectHelper("potion"));
        put("teleport", new TeleportFireEffectHelper("teleport"));
        
        //minor
        put("ground", new GroundFireEffectHelper("ground"));
    }};
    
    public static void addHelper(String type, FireEffectHelperInterface helper)
    {
        fireEffectHelpers.put(type, helper);
    }
    
    public static void addMajorHelper(String type, FireEffectHelperInterface helper)
    {
        majorEffectList.add(type);
        addHelper(type, helper);
    }
    
    public static void addMinorHelper(String type, FireEffectHelperInterface helper)
    {
        minorEffectList.add(type);
        addHelper(type, helper);
    }
    
    public static FireEffectHelperInterface getHelperByType(String type)
    {
        if(fireEffectHelpers.containsKey(type))
            return fireEffectHelpers.get(type);
        return exceptionCatchHelper;
    }
    
    public static int getColorByType(HashMap<String, String> data)
    {
        return getHelperByType(data.get("type")).getColor(data);
    }
    
    public static void triggerMajorEffects(ArrayList<HashMap<String, String>> majorEffects, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
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
        damageEntity(entity, amount, SpiritualFireBlockEffectConfig.FIRED_FLESH_TIME.get());
    }
    
    public static void damageEntity(LivingEntity entity, float amount, int cooldown)
    {
        if(amount == 0)
            return;
        
        entity.getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(playerSpiritualDamage -> {
            playerSpiritualDamage.addFleshDamage(amount);
        });
        
        entity.addEffect(new MobEffectInstance(Register.FIRED_FLESH.get(), cooldown, 99));
        
        // TODO: 2022/5/9 自行实现火焰遮挡视线

//                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
//                if(entity.getRemainingFireTicks() == 0)
//                {
//                    entity.setSecondsOnFire(8);
//                }
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
        }
        else
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
            stringBuilder.append(getHelperByType(one.get("type")).getJEIString(one)).append(";");
        }
        for(HashMap<String, String> one : minorEffects)
        {
            stringBuilder.append(getHelperByType(one.get("type")).getJEIString(one)).append(";");
        }
        
        return stringBuilder.toString();
    }
    
    public static void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        fireEffectHelpers.forEach((s, helper) -> helper.fillItemCategory(items, item));
    }
}
