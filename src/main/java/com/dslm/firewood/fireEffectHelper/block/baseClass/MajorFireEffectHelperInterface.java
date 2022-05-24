package com.dslm.firewood.fireEffectHelper.block.baseClass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

public interface MajorFireEffectHelperInterface extends FireEffectHelperInterface
{
    default HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity,
                                                  ArrayList<HashMap<String, String>> majorEffects,
                                                  ArrayList<HashMap<String, String>> minorEffects)
    {
        return triggerEffect(data, state, level, pos, entity);
    }
    
    HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity);
    
    float getDamage(HashMap<String, String> data);
    
    default float getMinHealth(HashMap<String, String> data)
    {
        return getDamage(data);
    }
}
