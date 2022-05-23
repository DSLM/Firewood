package com.dslm.firewood.fireEffectHelper.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public interface MinorFireEffectHelperInterface extends FireEffectHelperInterface
{
    void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos);
}
