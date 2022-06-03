package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.base.MajorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MinorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class ExceptionCatchHelper extends FireEffectHelperBase implements MajorFireEffectHelperInterface, MinorFireEffectHelperInterface
{
    public ExceptionCatchHelper()
    {
        super(new FireEffectNBTData(), "EXCEPTION");
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        return 0;
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, BlockState state, Level level, BlockPos pos, LivingEntity entity, ArrayList<FireEffectNBTData> majorEffects, ArrayList<FireEffectNBTData> minorEffects)
    {
        return MajorFireEffectHelperInterface.super.triggerEffect(data, tinderSourceType, state, level, pos, entity, majorEffects, minorEffects);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return MajorFireEffectHelperInterface.super.triggerEffect(data, tinderSourceType, state, level, pos, entity);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, BlockState state, Level level, BlockPos pos, ArrayList<FireEffectNBTData> majorEffects, ArrayList<FireEffectNBTData> minorEffects)
    {
        return MinorFireEffectHelperInterface.super.triggerEffect(data, tinderSourceType, state, level, pos, majorEffects, minorEffects);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, BlockState state, Level level, BlockPos pos)
    {
        return MinorFireEffectHelperInterface.super.triggerEffect(data, tinderSourceType, state, level, pos);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, Level level, BlockPos pos)
    {
        return MajorFireEffectHelperInterface.super.triggerEffect(data, tinderSourceType, level, pos);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, Level level, BlockPos pos)
    {
        return MajorFireEffectHelperInterface.super.triggerEffect(data, level, pos);
    }
    
    @Override
    public float getDamage(FireEffectNBTData data)
    {
        return 0;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(new TranslatableComponent("tooltip.firewood.tinder_item.exception_effect"));
        if(extended)
        {
            lines.add(new TranslatableComponent(data.toString()));
        }
        return lines;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return false;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        for(String i : data.keySet())
        {
            tags.putString(i, data.get(i));
        }
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        for(String i : tags.getAllKeys())
        {
            data.put(i, tags.getString(i));
        }
        return data;
    }
}
