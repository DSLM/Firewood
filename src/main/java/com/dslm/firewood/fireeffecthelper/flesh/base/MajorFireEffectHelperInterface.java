package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.config.SpiritualFireBlockEffectConfig;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireeffecthelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MajorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                     BlockState state, Level level, BlockPos pos, LivingEntity entity,
                                                     ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                     ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        return triggerEffect(data, tinderSourceType, state, level, pos, entity);
    }
    
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                     BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return triggerEffect(data, tinderSourceType, level, pos);
    }
    
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                     Level level, BlockPos pos)
    {
        return triggerEffect(data, level, pos);
    }
    
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, Level level, BlockPos pos)
    {
        return data;
    }
    
    default FireEffectNBTDataInterface cacheClear(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                  BlockState state, Level level, BlockPos pos,
                                                  ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                  ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        return cacheClear(data, tinderSourceType, state, level, pos);
    }
    
    default FireEffectNBTDataInterface cacheClear(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                  BlockState state, Level level, BlockPos pos)
    {
        return cacheClear(data, tinderSourceType, level, pos);
    }
    
    default FireEffectNBTDataInterface cacheClear(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                  Level level, BlockPos pos)
    {
        return cacheClear(data, level, pos);
    }
    
    default FireEffectNBTDataInterface cacheClear(FireEffectNBTDataInterface data, Level level, BlockPos pos)
    {
        return data;
    }
    
    float getDamage(FireEffectNBTDataInterface data);
    
    default float getMinHealth(FireEffectNBTDataInterface data)
    {
        return getDamage(data);
    }
    
    default int getCooldown(FireEffectNBTDataInterface data)
    {
        return SpiritualFireBlockEffectConfig.FIRED_FLESH_TIME.get();
    }
}
