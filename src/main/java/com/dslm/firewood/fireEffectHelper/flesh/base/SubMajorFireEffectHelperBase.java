package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import com.dslm.firewood.subType.FireEffectSubTypeBase;
import com.dslm.firewood.subType.FireEffectSubTypeManager;
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
import net.minecraft.world.phys.AABB;

import java.util.*;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public abstract class SubMajorFireEffectHelperBase extends MajorFireEffectHelperBase
{
    public enum TargetType
    {
        BLOCK,
        LIVING_ENTITY
    }
    
    public final TargetType targetType;
    
    public SubMajorFireEffectHelperBase(FireEffectNBTDataInterface defaultData, String id, TargetType targetType)
    {
        super(defaultData, id);
        this.targetType = targetType;
    }
    
    public SubMajorFireEffectHelperBase(String id, TargetType targetType)
    {
        this(new FireEffectNBTData()
        {{
            put(StaticValue.TYPE, id);
            put(StaticValue.SUB_TYPE, "");
            put(StaticValue.PROCESS, "0");
        }}, id, targetType);
    }
    
    public String getSubType(FireEffectNBTDataInterface data)
    {
        return data.getSubType();
    }
    
    public String getRealProcess(FireEffectNBTDataInterface data)
    {
        return data.get(StaticValue.PROCESS);
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(StaticValue.SUB_TYPE, data.getSubType());
        tags.putString(StaticValue.PROCESS, data.get(StaticValue.PROCESS));
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = new FireEffectNBTData();
        data.put(StaticValue.TYPE, ID);
        data.put(StaticValue.SUB_TYPE, tags.get(StaticValue.SUB_TYPE).getAsString());
        data.put(StaticValue.PROCESS, tags.get(StaticValue.PROCESS).getAsString());
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(getMainToolTips(data, extended));
        if(extended)
        {
            lines.addAll(getExtraToolTips(data));
        }
        return lines;
    }
    
    public Component getMainToolTips(FireEffectNBTDataInterface data, boolean extended)
    {
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
        mainLine.setCooldown(getCooldown(data));
        return mainLine;
    }
    
    public ArrayList<Component> getExtraToolTips(FireEffectNBTDataInterface data)
    {
        ArrayList<Component> lines = new ArrayList<>();
        if(getProcess(data) > 1)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.now_progress", getRealProcess(data), getProcess(data)),
                    getColor(data)));
        }
        if(getChance(data) < 100)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.chance", getChance(data)),
                    getColor(data)));
        }
        lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.range", getRange(data)),
                getColor(data)));
        return lines;
    }
    
    @Override
    public String getJEIString(FireEffectNBTDataInterface data)
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
    }
    
    @Override
    public float getDamage(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getDamage();
    }
    
    @Override
    public int getColor(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getColor();
    }
    
    public int getRange(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getRange();
    }
    
    public int getProcess(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getProcess();
    }
    
    public float getChance(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getChance();
    }
    
    @Override
    public float getMinHealth(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? super.getMinHealth(data) : effectData.getMinHealth();
    }
    
    @Override
    public int getCooldown(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? super.getCooldown(data) : effectData.getCooldown();
    }
    
    public FireEffectSubTypeBase getSubRealEffect(FireEffectNBTDataInterface data)
    {
        return getSubMap().getOrDefault(data.getSubType(), null);
    }
    
    public HashMap<String, FireEffectSubTypeBase> getSubMap()
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
    
    public static Iterable<BlockPos> getBlockPosIterable(BlockPos pos, int offset)
    {
        return BlockPos.betweenClosed(
                pos.offset(offset, offset, offset),
                pos.offset(-offset, -offset, -offset));
    }
    
    public static Iterable<LivingEntity> getEntityPosIterable(BlockPos pos, Level level, LivingEntity livingEntity, int offset)
    {
        if(offset < 0) return Collections.singleton(livingEntity);
        return level.getEntities(null, new AABB(
                pos.offset(offset, offset, offset),
                pos.offset(-offset, -offset, -offset)))
                .stream().filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .toList();
    }
    
    public static void dropItemAt(Level level, BlockPos pos, ItemStack itemStack)
    {
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack.copy()));
    }
    
    @Override
    public FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                    BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return switch(targetType)
                {
                    case BLOCK -> checkBlocks(data, level, pos);
                    case LIVING_ENTITY -> checkEntitys(data, level, pos, entity);
                };
    }
    
    public FireEffectNBTDataInterface checkBlocks(FireEffectNBTDataInterface data, Level level, BlockPos pos)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(effectData == null)
            return data;
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
    
    public void transmuteBlock(FireEffectNBTDataInterface data, BlockState blockState, Level level, BlockPos blockPos)
    {
    
    }
    
    public FireEffectNBTDataInterface checkEntitys(FireEffectNBTDataInterface data, Level level, BlockPos pos, LivingEntity entity)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(effectData == null)
            return data;
        int nowProccess = Integer.parseInt(data.get(StaticValue.PROCESS)) + 1;
        if(nowProccess < effectData.getProcess())
        {
            data.put(StaticValue.PROCESS, String.valueOf(nowProccess));
            return data;
        }
        data.put(StaticValue.PROCESS, "0");
        
        Random random = level.random;
        getEntityPosIterable(pos, level, entity, effectData.getRange()).forEach(livingEntity -> {
            if(random.nextDouble() * 100 < effectData.getChance())
            {
                transmuteEntity(data, level, livingEntity);
            }
        });
        return data;
    }
    
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity)
    {
    
    }
}
