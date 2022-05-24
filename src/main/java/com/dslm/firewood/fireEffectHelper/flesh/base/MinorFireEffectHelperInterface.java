package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MinorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos,
                                            ArrayList<FireEffectNBTData> majorEffects,
                                            ArrayList<FireEffectNBTData> minorEffects)
    {
        return triggerEffect(data, state, level, pos);
    }
    
    FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos);
    
    default boolean canBePlacedOn(FireEffectNBTData data, Level level, BlockPos pos)
    {
        return true;
    }
}
