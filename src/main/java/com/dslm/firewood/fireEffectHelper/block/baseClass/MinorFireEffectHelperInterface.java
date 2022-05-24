package com.dslm.firewood.fireEffectHelper.block.baseClass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

public interface MinorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos,
                                                  ArrayList<HashMap<String, String>> majorEffects,
                                                  ArrayList<HashMap<String, String>> minorEffects)
    {
        return triggerEffect(data, state, level, pos);
    }
    
    HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos);
    
    default boolean canBePlacedOn(HashMap<String, String> data, Level level, BlockPos pos)
    {
        return true;
    }
}
