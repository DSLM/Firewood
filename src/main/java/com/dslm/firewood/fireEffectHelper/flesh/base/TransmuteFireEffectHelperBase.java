package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectSubType;
import com.dslm.firewood.recipe.FireEffectSubTypeManager;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public abstract class TransmuteFireEffectHelperBase extends MajorFireEffectHelperBase
{
    
    public TransmuteFireEffectHelperBase(FireEffectNBTData defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public TransmuteFireEffectHelperBase(String id)
    {
        super(new FireEffectNBTData()
        {{
            put(StaticValue.TYPE, id);
            put(StaticValue.SUB_TYPE, "");
            put(StaticValue.PROCESS, "0");
        }}, id);
    }
    
    public String getSubId(FireEffectNBTData data)
    {
        return data.getSubType();
    }
    
    public String getRealProcess(FireEffectNBTData data)
    {
        return data.get(StaticValue.PROCESS);
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(StaticValue.SUB_TYPE, data.getSubType());
        tags.putString(StaticValue.PROCESS, data.get(StaticValue.PROCESS));
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        data.put(StaticValue.TYPE, ID);
        data.put(StaticValue.SUB_TYPE, tags.getString(StaticValue.SUB_TYPE));
        data.put(StaticValue.PROCESS, String.valueOf(tags.getInt(StaticValue.PROCESS)));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                extended ?
                        new MiddleComponent("tooltip.firewood.tinder_item.multi_tooltip_format",
                                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%s".formatted(data.getType())),
                                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(data.getType(), data.getSubType())))
                        :
                        new MiddleComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(data.getType(), data.getSubType())),
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
        return data.getType() + "-" + data.getSubType();
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return super.isSameNBT(first, second) &&
                first.getString(StaticValue.SUB_TYPE).equals(second.getString(StaticValue.SUB_TYPE));
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(String s : getSubIdList())
        {
    
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, new FireEffectNBTData()
            {{
                put(StaticValue.SUB_TYPE, s);
                put(StaticValue.PROCESS, "0");
            }});
            
            if(!stack.isEmpty())
                items.add(stack);
        }
        super.fillItemCategory(items, item);
    }
    
    @Override
    public float getDamage(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getDamage();
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getColor();
    }
    
    public int getRange(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getRange();
    }
    
    public int getProcess(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getProcess();
    }
    
    public float getChance(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getChance();
    }
    
    @Override
    public float getMinHealth(FireEffectNBTData data)
    {
        FireEffectSubType effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getMinHealth();
    }
    
    public FireEffectSubType getSubRealEffect(FireEffectNBTData data)
    {
        return getSubMap().getOrDefault(data.getSubType(), null);
    }
    
    public HashMap<String, FireEffectSubType> getSubMap()
    {
        return FireEffectSubTypeManager.getEffectsMap().getOrDefault(getResourceName(), new HashMap<>());
    }
    
    public Set<String> getSubIdList()
    {
        return getSubMap().keySet();
    }
    
    public String getResourceName()
    {
        return "%s:%s".formatted(StaticValue.MOD_ID, getId());
    }
    
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return checkBlocks(data, state, level, pos, entity);
    }
    
    
    public FireEffectNBTData checkBlocks(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
    
        FireEffectSubType effectData = getSubRealEffect(data);
        int nowProccess = Integer.parseInt(data.get(StaticValue.PROCESS)) + 1;
        if(nowProccess < effectData.getProcess())
        {
            data.put(StaticValue.PROCESS, String.valueOf(nowProccess));
            return data;
        }
        data.put(StaticValue.PROCESS, "0");
        Random random = level.random;
        getBlockPosIterable(pos, effectData.getRange()).forEach(blockPos -> {
            if(random.nextDouble() * 100 < effectData.getChance())
            {
                BlockState blockState = level.getBlockState(blockPos);
                transmuteBlock(data, blockState, level, blockPos);
            }
        });
        return data;
    }
    
    public abstract void transmuteBlock(FireEffectNBTData data, BlockState blockState, Level level, BlockPos blockPos);
}
