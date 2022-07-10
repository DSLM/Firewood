package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.fireeffecthelper.flesh.BlockCheckOrderFireEffectHelper;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireeffecthelper.flesh.data.TinderSourceType;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.FireEffectSubTypeManager;
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
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers.getMinorHelperByType;
import static com.dslm.firewood.util.StaticValue.colorfulText;

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
            setType(id);
            setSubType("");
            setProcess(0);
        }}, id, targetType);
    }
    
    public String getSubType(FireEffectNBTDataInterface data)
    {
        return data.getSubType();
    }
    
    public int getRealProcess(FireEffectNBTDataInterface data)
    {
        return data.getProcess();
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(StaticValue.SUB_TYPE, data.getSubType());
        if(data.getProcess() != Integer.MIN_VALUE)
        {
            tags.putInt(StaticValue.PROCESS, data.getProcess());
        }
        if(data.isInCache())
        {
            tags.putBoolean(StaticValue.IN_CACHE, data.isInCache());
        }
        int[] cache = data.getCache();
        int tempCount = 0;
        for(int i = 0; i < cache.length; i++)
        {
            if(cache[i] == Integer.MIN_VALUE)
            {
                tempCount++;
            }
        }
        if(tempCount < cache.length)
        {
            tags.putIntArray(StaticValue.CACHE, data.getCache());
        }
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = new FireEffectNBTData();
        data.setType(ID);
        data.setSubType(tags.get(StaticValue.SUB_TYPE).getAsString());
        if(tags.contains(StaticValue.PROCESS))
        {
            data.setProcess(tags.getInt(StaticValue.PROCESS));
        }
        if(tags.contains(StaticValue.IN_CACHE))
        {
            data.setInCache(tags.getBoolean(StaticValue.IN_CACHE));
        }
        if(tags.contains(StaticValue.CACHE))
        {
            data.setCache(tags.getIntArray(StaticValue.CACHE));
        }
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
        int[] range = getRange(data);
        if(range[0] < 0 || range[1] < 0 || range[2] < 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.range_single"),
                    getColor(data)));
        }
        else
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.recipe.range", range[0], range[1], range[2]),
                    getColor(data)));
        }
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
            FireEffectNBTDataInterface defaultData = getDefaultData();
            defaultData.setSubType(s);
    
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, defaultData);
    
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
    
    public int[] getRange(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        return effectData == null ? new int[]{0, 0, 0} : effectData.getRange();
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
        return FireEffectSubTypeManager.getEffectsMap().getOrDefault(getId(), new HashMap<>());
    }
    
    public Set<String> getSubIdList()
    {
        return getSubMap().keySet();
    }
    
    public static Iterable<LivingEntity> getEntityPosIterable(BlockPos pos, Level level, LivingEntity livingEntity, int[] offset)
    {
        if(offset[0] < 0 || offset[1] < 0 || offset[2] < 0) return Collections.singleton(livingEntity);
        return level.getEntities(null, new AABB(
                pos.offset(offset[0], offset[1], offset[2]),
                pos.offset(-offset[0], -offset[1], -offset[2])))
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
                                                    BlockState state, Level level, BlockPos pos, LivingEntity entity,
                                                    ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                    ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        return switch(targetType)
                {
                    case BLOCK -> checkBlocks(data, pos, minorEffects);
                    case LIVING_ENTITY -> checkEntities(data, level, pos, entity);
                };
    }
    
    public FireEffectNBTDataInterface checkBlocks(FireEffectNBTDataInterface data, BlockPos pos, ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        if(data.isInCache())
        {
            return data;
        }
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(effectData == null)
            return data;
        int nowProcess = data.getProcess() + 1;
        if(nowProcess < effectData.getProcess())
        {
            data.setProcess(nowProcess);
            return data;
        }
        data.setProcess(0);
        data.setInCache(true);
        BlockCheckOrderFireEffectHelper blockCheckOrderFireEffectHelper = (BlockCheckOrderFireEffectHelper) getMinorHelperByType("order");
        String nowOrder = "y+x-z-";
        for(var effect : minorEffects)
        {
            if(effect.getType().equals("order"))
            {
                nowOrder = effect.get("order");
            }
        }
        
        data.setCache(blockCheckOrderFireEffectHelper.initializeCash(pos, effectData, nowOrder));
        
        return data;
    }
    
    @Override
    public FireEffectNBTDataInterface cacheClear(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                 BlockState state, Level level, BlockPos pos,
                                                 ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                 ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        if(!data.isInCache())
        {
            return data;
        }
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        Random random = level.random;
        
        BlockCheckOrderFireEffectHelper blockCheckOrderFireEffectHelper = (BlockCheckOrderFireEffectHelper) getMinorHelperByType("order");
        String nowOrder = "y+x-z-";
        for(var effect : minorEffects)
        {
            if(effect.getType().equals("order"))
            {
                nowOrder = effect.get("order");
            }
        }
        Pair<Iterable<BlockPos>, FireEffectNBTDataInterface> tempData =
                blockCheckOrderFireEffectHelper.getBlocksByCache(data, effectData, effectData.getTargetLimit(), nowOrder);
        
        tempData.getLeft().forEach(blockPos -> {
            if(random.nextDouble() * 100 < effectData.getChance())
            {
                BlockState blockState = level.getBlockState(blockPos);
                transmuteBlock(data, blockState, level, blockPos);
            }
        });
        
        return tempData.getRight();
    }
    
    public void transmuteBlock(FireEffectNBTDataInterface data, BlockState blockState, Level level, BlockPos blockPos)
    {
    
    }
    
    public FireEffectNBTDataInterface checkEntities(FireEffectNBTDataInterface data, Level level, BlockPos pos, LivingEntity entity)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(effectData == null)
            return data;
        int nowProcess = data.getProcess() + 1;
        if(nowProcess < effectData.getProcess())
        {
            data.setProcess(nowProcess);
            return data;
        }
        data.setProcess(0);
    
        Random random = level.random;
        getEntityPosIterable(pos, level, entity, effectData.getRange()).forEach(livingEntity -> {
            if(random.nextDouble() * 100 < effectData.getChance())
            {
                transmuteEntity(data, level, livingEntity, entity);
            }
        });
        return data;
    }
    
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity, LivingEntity source)
    {
    
    }
}
