package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MajorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType,
                                            BlockState state, Level level, BlockPos pos, LivingEntity entity,
                                            ArrayList<FireEffectNBTData> majorEffects,
                                            ArrayList<FireEffectNBTData> minorEffects)
    {
        return triggerEffect(data, tinderSourceType, state, level, pos, entity);
    }
    
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType,
                                            BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return triggerEffect(data, tinderSourceType, level, pos);
    }
    
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType,
                                            Level level, BlockPos pos)
    {
        return triggerEffect(data, level, pos);
    }
    
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, Level level, BlockPos pos)
    {
        return data;
    }
    
    float getDamage(FireEffectNBTData data);
    
    default float getMinHealth(FireEffectNBTData data)
    {
        return getDamage(data);
    }
}
