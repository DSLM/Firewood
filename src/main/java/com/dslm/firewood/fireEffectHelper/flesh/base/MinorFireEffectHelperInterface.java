package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MinorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                     BlockState state, Level level, BlockPos pos,
                                                     ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                     ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        return triggerEffect(data, tinderSourceType, state, level, pos);
    }
    
    default FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType,
                                                     BlockState state, Level level, BlockPos pos)
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
    
    default boolean canBePlacedOn(FireEffectNBTDataInterface data, Level level, BlockPos pos)
    {
        return true;
    }
}
