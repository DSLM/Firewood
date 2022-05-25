package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TransformFireEffectType;
import com.dslm.firewood.recipe.TransformEffectTypeManager;
import com.dslm.firewood.tooltip.MiddleComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public abstract class TransformFireEffectHelperBase extends MajorFireEffectHelperBase
{
    public static String SUB_TAG_ID = "subType";
    public static String PROCESS = "process";
    
    public TransformFireEffectHelperBase(FireEffectNBTData defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public TransformFireEffectHelperBase(String id)
    {
        super(id);
    }
    
    public String getSubId(FireEffectNBTData data)
    {
        return data.get(SUB_TAG_ID);
    }
    
    public String getRealProcess(FireEffectNBTData data)
    {
        return data.get(PROCESS);
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(TYPE, ID);
        tags.putString(SUB_TAG_ID, data.get(SUB_TAG_ID));
        tags.putString(PROCESS, data.get(PROCESS));
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        data.put(TYPE, ID);
        data.put(SUB_TAG_ID, tags.getString(SUB_TAG_ID));
        data.put(PROCESS, String.valueOf(tags.getInt(PROCESS)));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.multi_tooltip_format",
                        new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%s".formatted(data.getType())),
                        new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(data.getType(), data.get(SUB_TAG_ID)))),
                getColor(data));
        mainLine.setDamage(getDamage(data));
        mainLine.setMinHealth(getMinHealth(data));
        lines.add(mainLine);
        if(extended)
        {
            lines.addAll(getExtraToolTips(data));
        }
        return lines;
    }
    
    public ArrayList<Component> getExtraToolTips(FireEffectNBTData data)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.now_progress", getRealProcess(data), getProcess(data)),
                getColor(data)));
        if(getChance(data) < 100)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.chance", getChance(data)),
                    getColor(data)));
        }
        lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.range", getRange(data)),
                getColor(data)));
        return lines;
    }
    
    public static Iterable<BlockPos> getBlockPosIterable(BlockPos pos, int offset)
    {
        return BlockPos.betweenClosed(
                pos.offset(offset, offset, offset),
                pos.offset(-offset, -offset, -offset));
    }
    
    public static void dropItemAt(Level level, BlockPos pos, ItemStack itemStack)
    {
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack.copy()));
        
        return;
    }
    
    @Override
    public String getJEIString(FireEffectNBTData data)
    {
        return data.getType() + "-" + data.get(SUB_TAG_ID);
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return super.isSameNBT(first, second) &&
                first.getString(SUB_TAG_ID).equals(second.getString(SUB_TAG_ID));
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(String s : getSubIdList())
        {
    
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, new FireEffectNBTData()
            {{
                put(SUB_TAG_ID, s);
                put(PROCESS, "0");
            }});
            
            if(!stack.isEmpty())
                items.add(stack);
        }
        super.fillItemCategory(items, item);
    }
    
    @Override
    public float getDamage(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getDamage();
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getColor();
    }
    
    public int getRange(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getRange();
    }
    
    public int getProcess(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getProcess();
    }
    
    public float getChance(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getChance();
    }
    
    @Override
    public float getMinHealth(FireEffectNBTData data)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getMinHealth();
    }
    
    public TransformFireEffectType getSubRealEffect(FireEffectNBTData data)
    {
        return getSubMap().getOrDefault(data.get(SUB_TAG_ID), null);
    }
    
    public HashMap<String, TransformFireEffectType> getSubMap()
    {
        return TransformEffectTypeManager.getEffectsMap().getOrDefault(getResourceName(), new HashMap<>());
    }
    
    public Set<String> getSubIdList()
    {
        return getSubMap().keySet();
    }
    
    public String getResourceName()
    {
        return "%s:%s".formatted(Firewood.MOD_ID, getId());
    }
}
