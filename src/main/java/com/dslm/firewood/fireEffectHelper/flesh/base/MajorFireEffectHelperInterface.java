package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface MajorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity,
                                            ArrayList<FireEffectNBTData> majorEffects,
                                            ArrayList<FireEffectNBTData> minorEffects)
    {
        return triggerEffect(data, state, level, pos, entity);
    }
    
    FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity);
    
    float getDamage(FireEffectNBTData data);
    
    default float getMinHealth(FireEffectNBTData data)
    {
        return getDamage(data);
    }
}
