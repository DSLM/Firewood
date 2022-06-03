package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MinorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType,
                                            BlockState state, Level level, BlockPos pos,
                                            ArrayList<FireEffectNBTData> majorEffects,
                                            ArrayList<FireEffectNBTData> minorEffects)
    {
        return triggerEffect(data, tinderSourceType, state, level, pos);
    }
    
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType,
                                            BlockState state, Level level, BlockPos pos)
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
    
    default boolean canBePlacedOn(FireEffectNBTData data, Level level, BlockPos pos)
    {
        return true;
    }
}
